#!/usr/bin/env bash
set -euo pipefail
WS="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-monitoring-system-217467-217482/upstdc_backend"
cd "$WS"
# locate jar: prefer fat jar with BOOT-INF
JAR=""
for j in target/*.jar; do [ -f "$j" ] || continue; if unzip -l "$j" 2>/dev/null | grep -q 'BOOT-INF'; then JAR="$j"; break; fi; done
if [ -z "$JAR" ]; then JAR=$(ls -1t target/*.jar 2>/dev/null | head -n1 || true); fi
[ -n "$JAR" ] || { echo 'jar not found in target/' >&2; exit 7; }
# prepare env and storage dir
export SERVER_PORT=${SERVER_PORT:-8080}
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-dev}
export FILE_STORAGE_PATH=${FILE_STORAGE_PATH:-$WS/storage}
mkdir -p "$FILE_STORAGE_PATH"
LOG=/tmp/upstdc_app.log
# start app in background
nohup java -jar "$JAR" --server.port=${SERVER_PORT} >"$LOG" 2>&1 &
APP_PID=$!
# short grace period
sleep 2
if ! kill -0 "$APP_PID" >/dev/null 2>&1; then echo "app process died immediately; tail log:" >&2; tail -n 200 "$LOG" >&2 || true; exit 8; fi
# expose pid and log path
echo "APP_PID=$APP_PID"
echo "LOG=$LOG"
