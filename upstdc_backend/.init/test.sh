#!/usr/bin/env bash
set -euo pipefail
WS="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-monitoring-system-217467-217482/upstdc_backend"
cd "$WS"
if [ -x ./mvnw ]; then MVN_CMD=./mvnw; else MVN_CMD=$(command -v mvn || true); fi
[ -n "${MVN_CMD:-}" ] || { echo 'mvn not available' >&2; exit 6; }
# run tests with dev profile; if they are heavy, they will fail fast
SPRING_PROFILES_ACTIVE=dev "$MVN_CMD" -B test || { echo 'tests failed' >&2; exit 5; }
