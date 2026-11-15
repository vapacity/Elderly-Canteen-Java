#!/usr/bin/env bash
set -euo pipefail
demo="$(cd "$(dirname "$0")" && pwd)"
repo="$(dirname "$demo")"
host="${1:-root@116.62.84.213}"
archive="$(mktemp -t canteen-release)"
trap 'rm -f "$archive"' EXIT
COPYFILE_DISABLE=1 tar -C "$demo/public" -czf "$archive" .
ssh -o BatchMode=yes "$host" '
  set -eu
  getent passwd canteen-demo >/dev/null || useradd -r -s /sbin/nologin canteen-demo
  install -d -m 0755 /opt/canteen-demo /var/www/canteen-demo
  if [ ! -s /etc/canteen-demo.env ]; then
    umask 077
    printf "CANTEEN_DEMO_PASSWORD=%s\nCANTEEN_JWT_SECRET=%s\n" "$(openssl rand -hex 8)" "$(openssl rand -hex 32)" > /etc/canteen-demo.env
  fi
  if ! grep -q "^CANTEEN_ADMIN_PASSWORD=" /etc/canteen-demo.env; then
    printf "CANTEEN_ADMIN_PASSWORD=%s\n" "$(openssl rand -hex 16)" >> /etc/canteen-demo.env
  fi
'
scp -q -o BatchMode=yes "$repo/target/ElderlyCanteen-0.0.1-SNAPSHOT.jar" "$host:/opt/canteen-demo/canteen.jar.new"
scp -q -o BatchMode=yes "$archive" "$host:/opt/canteen-demo/frontend.tar.gz"
scp -q -o BatchMode=yes "$demo/nginx.conf" "$host:/etc/nginx/conf.d/canteen-demo.conf"
scp -q -o BatchMode=yes "$demo/canteen-demo.service" "$host:/etc/systemd/system/canteen-demo.service"
ssh -o BatchMode=yes "$host" '
  set -eu
  nginx -t
  tar -xzf /opt/canteen-demo/frontend.tar.gz -C /var/www/canteen-demo
  chmod a+rX -R /var/www/canteen-demo
  mv /opt/canteen-demo/canteen.jar.new /opt/canteen-demo/canteen.jar
  systemctl daemon-reload
  systemctl enable canteen-demo nginx
  systemctl restart canteen-demo nginx
'
