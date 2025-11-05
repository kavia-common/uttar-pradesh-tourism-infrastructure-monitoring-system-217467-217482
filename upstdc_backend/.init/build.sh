#!/usr/bin/env bash
set -euo pipefail
WS="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-monitoring-system-217467-217482/upstdc_backend"
cd "$WS"
# pick mvn command: prefer ./mvnw if executable
if [ -x ./mvnw ]; then MVN_CMD=./mvnw; else MVN_CMD=$(command -v mvn || true); fi
[ -n "${MVN_CMD:-}" ] || { echo 'mvn not available' >&2; exit 6; }
# build without tests to produce jar quickly
"$MVN_CMD" -B -DskipTests package >/dev/null 2>&1 || { echo 'build failed' >&2; exit 6; }
