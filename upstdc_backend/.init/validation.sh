#!/usr/bin/env bash
set -euo pipefail
WS="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-monitoring-system-217467-217482/upstdc_backend"
cd "$WS"
# ensure mvn available and build
if [ -x ./mvnw ]; then MVN_CMD=./mvnw; else MVN_CMD=$(command -v mvn || true); fi
[ -n "${MVN_CMD:-}" ] || { echo 'mvn not available' >&2; exit 6; }
# build (skip tests to be fast)
"$MVN_CMD" -B -DskipTests package >/dev/null 2>&1 || { echo 'build failed' >&2; exit 6; }
# find fat jar
JAR=""
for j in target/*.jar; do [ -f "$j" ] || continue; if unzip -l "$j" 2>/dev/null | grep -q 'BOOT-INF'; then JAR="$j"; break; fi; done
if [ -z "$JAR" ]; then JAR=$(ls -1t target/*.jar 2>/dev/null | head -n1 || true); fi
[ -n "$JAR" ] || { echo 'jar not found in target/' >&2; exit 7; }
# env and storage
SERVER_PORT=${SERVER_PORT:-8080}
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}
export FILE_STORAGE_PATH=${FILE_STORAGE_PATH:-$WS/storage}
mkdir -p "$FILE_STORAGE_PATH"
LOG=/tmp/upstdc_app.log
# start
nohup java -jar "$JAR" --server.port=${SERVER_PORT} >"$LOG" 2>&1 &
APP_PID=$!
sleep 2
if ! kill -0 "$APP_PID" >/dev/null 2>&1; then echo "app process died immediately; tail log:" >&2; tail -n 300 "$LOG" >&2 || true; exit 8; fi
# ensure cleanup on exit
trap 'kill -TERM "$APP_PID" >/dev/null 2>&1 || true; sleep 3; kill -KILL "$APP_PID" >/dev/null 2>&1 || true' EXIT INT TERM
# wait for actuator health
TIMEOUT=60; SECS=0
while true; do
  if command -v jq >/dev/null 2>&1; then
    STATUS=$(curl -sS "http://127.0.0.1:${SERVER_PORT}/actuator/health" 2>/dev/null | jq -r '.status' 2>/dev/null || true)
    [ "$STATUS" = "UP" ] && break
  else
    RESP=$(curl -sS "http://127.0.0.1:${SERVER_PORT}/actuator/health" 2>/dev/null || true)
    echo "$RESP" | grep -q 'UP' && break
  fi
  sleep 1; SECS=$((SECS+1))
  if [ $SECS -ge $TIMEOUT ]; then
    echo "app did not report UP in ${TIMEOUT}s; tail log:" >&2
    tail -n 400 "$LOG" >&2 || true
    kill -TERM "$APP_PID" 2>/dev/null || true
    exit 9
  fi
done
# evidence
curl -sS "http://127.0.0.1:${SERVER_PORT}/actuator/health" || true
echo "APP_PID=$APP_PID"
# graceful shutdown
kill -TERM "$APP_PID" || true
for _ in {1..10}; do if ! kill -0 "$APP_PID" >/dev/null 2>&1; then echo "process $APP_PID stopped"; break; fi; sleep 1; done
if kill -0 "$APP_PID" >/dev/null 2>&1; then kill -KILL "$APP_PID" >/dev/null 2>&1 || true; fi
trap - EXIT INT TERM
