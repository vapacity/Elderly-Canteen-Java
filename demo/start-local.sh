#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p data
if [[ ! -s data/demo.env ]]; then
    (umask 077; printf 'CANTEEN_DEMO_PASSWORD=%s\nCANTEEN_JWT_SECRET=%s\n' "$(openssl rand -hex 8)" "$(openssl rand -hex 32)" > data/demo.env)
fi
set -a
source data/demo.env
set +a
if [[ ! -f demo/public/mobile/index.html || ! -f demo/public/index.html || ! -f target/ElderlyCanteen-0.0.1-SNAPSHOT.jar ]]; then
    echo 'Build frontend assets and the backend JAR first.' >&2
    exit 1
fi
java_bin="${JAVA_HOME:+$JAVA_HOME/bin/}java"
"$java_bin" -Xmx320m -Duser.timezone=Asia/Shanghai -jar target/ElderlyCanteen-0.0.1-SNAPSHOT.jar --spring.profiles.active=demo &
backend=$!
python3 demo/serve-local.py &
gateway=$!
trap 'kill "$backend" "$gateway" 2>/dev/null || true' EXIT
trap 'exit 0' INT TERM
echo '网页端 http://127.0.0.1:18080/ | 手机端 http://127.0.0.1:18080/mobile/'
wait "$backend"
