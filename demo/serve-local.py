"""Serve built demo assets and forward demo API calls to loopback Java."""
import argparse
import functools
import http.client
import pathlib
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlsplit


class Gateway(SimpleHTTPRequestHandler):
    def dispatch(self):
        path = urlsplit(self.path).path
        if path.startswith('/demo-api/'):
            connection = http.client.HTTPConnection('127.0.0.1', 18180, timeout=20)
            headers = {key: self.headers[key] for key in ('Content-Type', 'Authorization')
                       if key in self.headers}
            size = int(self.headers.get('Content-Length', '0'))
            try:
                connection.request(self.command, self.path, self.rfile.read(size) if size else None, headers)
                response = connection.getresponse()
                body = response.read()
                self.send_response(response.status)
                self.send_header('Content-Type', response.getheader('Content-Type', 'application/octet-stream'))
                self.send_header('Content-Length', str(len(body)))
                self.end_headers()
                if self.command != 'HEAD':
                    self.wfile.write(body)
            except (OSError, http.client.HTTPException):
                self.send_error(502, 'Java demo backend unavailable')
            finally:
                connection.close()
        elif self.command in ('GET', 'HEAD'):
            if not path.startswith(('/mobile/', '/demo-img/')) and not pathlib.Path(self.translate_path(path)).exists():
                self.path = '/index.html'
            if self.command == 'GET':
                super().do_GET()
            else:
                super().do_HEAD()
        else:
            self.send_error(404)

    do_GET = do_HEAD = do_POST = do_DELETE = do_PUT = dispatch


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', type=int, default=18080)
    args = parser.parse_args()
    public = pathlib.Path(__file__).resolve().parent / 'public'
    with ThreadingHTTPServer(('127.0.0.1', args.port), functools.partial(Gateway, directory=public)) as server:
        server.serve_forever()
