#!/usr/bin/env bash
set -euo pipefail
demo="$(cd "$(dirname "$0")" && pwd)"
workspace="$(dirname "$(dirname "$demo")")"
web="$workspace/elderly_canteen_management_system_website_frontend"
mobile="$workspace/elderly_canteen_management_system_mobile_frontend"
for frontend in "$web" "$mobile"; do
  (cd "$frontend"; npm ci --cache "${TMPDIR:-/tmp}/canteen-npm-cache" --no-audit --no-fund; npm run build)
done
mkdir -p "$demo/public/mobile" "$demo/public/demo-img"
cp -R "$web/dist/." "$demo/public/"
cp -R "$mobile/dist/." "$demo/public/mobile/"
cp -R "$demo/images/." "$demo/public/demo-img/"
