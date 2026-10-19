# Deploy

Free setup with no domain:

- **Frontend** (`book-tribe-ui/`) on Vercel → `https://<app>.vercel.app`
- **Backend + Keycloak** on a home Ubuntu server with Docker Compose, published over HTTPS through **Tailscale Funnel** → `https://<host>.<tailnet>.ts.net`

```
Browser ──> https://<app>.vercel.app                      (static Angular build)
   ├──────> https://<host>.ts.net       (Funnel :443)  → 127.0.0.1:8088 gateway
   └──────> https://<host>.ts.net:8443  (Funnel :8443) → 127.0.0.1:9090 keycloak
```

HTTPS is mandatory on both backend URLs: a page served over HTTPS cannot call HTTP APIs (mixed content), and keycloak-js needs a secure context. Only the gateway and Keycloak are published, on `127.0.0.1`; Postgres and the other services stay on the internal Docker network. notification-service is not deployed because no service calls it yet.

## 0. Pick the URLs first

1. Create the Vercel project (step 3) to learn the `*.vercel.app` URL.
2. Install Tailscale on the server (step 1) to learn the `*.ts.net` hostname (`tailscale status --json | jq -r .Self.DNSName`, without the trailing dot).
3. Put the ts.net hostname in `book-tribe-ui/src/environments/environment.prod.ts` and commit.

## 1. Server (Ubuntu)

```bash
# Docker Engine + compose plugin: https://docs.docker.com/engine/install/ubuntu/
curl -fsSL https://tailscale.com/install.sh | sh
sudo tailscale up
```

In the Tailscale admin console:

- **DNS** → enable MagicDNS and **HTTPS Certificates**
- **Access controls** → allow Funnel for the node, e.g. add to the policy:
  ```json
  "nodeAttrs": [{ "target": ["autogroup:member"], "attr": ["funnel"] }]
  ```

Expect around 3 GB of RAM in use (5 JVMs capped at 256 MB heap each, plus Keycloak and Postgres).

## 2. Backend

```bash
git clone <repo> booktribe && cd booktribe/deploy
cp .env.example .env    # set PUBLIC_HOST, FRONTEND_URL and real passwords
docker compose -f docker-compose.prod.yml up -d --build
docker compose -f docker-compose.prod.yml ps   # wait until everything is healthy

sudo tailscale funnel --bg --https=443  http://127.0.0.1:8088
sudo tailscale funnel --bg --https=8443 http://127.0.0.1:9090
tailscale funnel status
```

The Funnel configuration persists across reboots, and the containers restart on their own (`restart: unless-stopped`).

Keycloak imports `keycloak/realm/book-social-network` on its **first** start only. `${FRONTEND_URL}` in the `bsn` client (redirect URIs and web origins) is replaced from `.env` at import time. If `FRONTEND_URL` changes later, update the `bsn` client in the Keycloak admin console (`https://<host>.ts.net:8443/admin`), because the realm is not re-imported once it exists.

The realm export contains test users. Delete them, or change their passwords, from the admin console before sharing the URL.

Update:

```bash
git pull && docker compose -f docker-compose.prod.yml up -d --build
```

## 3. Frontend (Vercel)

Import the GitHub repo into Vercel with:

| Setting | Value |
|---|---|
| Root Directory | `book-tribe-ui` |
| Framework Preset | Other |
| Build Command | `npm run build` |
| Output Directory | `dist/book-network-ui` |
| Node.js Version | 20.x |

`vercel.json` rewrites every path to `index.html`, so deep links such as `/books/my-books` survive a reload. Each push to the default branch triggers a new deploy.

## 4. Checks

```bash
H=<host>.ts.net
curl -s https://$H:8443/realms/book-social-network/.well-known/openid-configuration | jq -r .issuer
# → https://<host>.ts.net:8443/realms/book-social-network
curl -si https://$H/api/v1/books | head -1
# → HTTP/2 401
curl -si -X OPTIONS https://$H/api/v1/books \
  -H "Origin: https://<app>.vercel.app" -H "Access-Control-Request-Method: GET" | grep -i access-control-allow-origin
# → exactly one line with the Vercel origin
```

Then open the Vercel URL: you should land on the Keycloak login page and come back to the book list.
