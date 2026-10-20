# BookTribe

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen?logo=springboot)
![Angular](https://img.shields.io/badge/Angular-16-red?logo=angular)
![Keycloak](https://img.shields.io/badge/Keycloak-24.0.2-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

A full-stack book-sharing social network where users can lend, borrow, and review books — built as a portfolio project covering a Spring Cloud microservice architecture with Angular and Keycloak.

## Features

- Browse the community catalogue of shareable books (paginated)
- Publish your own books with cover image upload, toggle shareable / archived status
- Borrow a book and return it; owner explicitly approves the return
- Leave a star rating and written feedback on any book you have borrowed
- Registration, login and logout handled by Keycloak (JWT / OAuth2, PKCE)

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2.2 · Java 17 · Spring Security OAuth2 Resource Server |
| Microservices | Spring Cloud Config · Eureka · Spring Cloud Gateway · OpenFeign · Resilience4j |
| Database | PostgreSQL · JPA / Hibernate (`ddl-auto: update`) |
| Auth | Keycloak 24.0.2 — realm `book-social-network`, client `bsn` |
| Mail (dev) | MailDev |
| Frontend | Angular 16 · Bootstrap 5 · keycloak-js |
| API contract | OpenAPI 3 (Springdoc) · `ng-openapi-gen` |

## Architecture

BookTribe is a set of Spring Boot 3.2.2 microservices built on Spring Cloud. Every service registers with Eureka and loads its configuration from the Config Server; the Angular frontend talks only to the gateway.

| Service | Folder | Port | Responsibility |
|---|---|---|---|
| Config Server | `config-server/` | `8888` | Centralised configuration (native profile, files in `src/main/resources/config-repo/`) |
| Discovery Server | `discovery-server/` | `8761` | Eureka service registry |
| Gateway | `gateway/` | `8088` | Spring Cloud Gateway, single entry point: routes `/api/v1/books/**` to book-service and `/api/v1/feedbacks/**` to feedback-service |
| Book service | `book-tribe-be/` | `8091` | **Book** and **History** domains: catalogue, borrow/return/approve flow, cover upload |
| Feedback service | `feedback-service/` | `8082` | Star ratings and reviews, with its own database (`book_tribe_feedback`) |
| Notification service | `notification-service/` | `8083` | Thymeleaf email templates sent over SMTP (`POST /api/v1/notifications/email`) |

Book service and feedback service call each other through OpenFeign clients protected by Resilience4j circuit breakers and time limiters, with fallbacks when the other service is unavailable. A `BaseEntity` superclass provides JPA auditing fields on every entity.

All authentication is handled externally by Keycloak: book service and feedback service validate JWTs as OAuth2 resource servers and map Keycloak roles to Spring Security authorities via `KeycloakJwtAuthenticationConverter`.

The Angular frontend consumes the API through a fully generated client (`ng-openapi-gen`) that is regenerated from the OpenAPI spec whenever the backend contract changes. Keycloak-js handles the auth flow (`login-required` on app init) and an `HttpTokenInterceptor` attaches the Bearer token to every request.

Architecture diagrams are available in [`diagrams.drawio`](./diagrams.drawio) and rendered screenshots in [`screenshots/`](./screenshots/).

## Getting Started

**Prerequisites:** Docker · Java 17+ · Node 18+ · Angular CLI 16

### 1. Start the infrastructure

```bash
docker compose up -d
```

| Service  | URL / Port | Credentials |
|----------|-----------|-------------|
| Postgres | `localhost:5432` | `username` / `password` |
| MailDev  | `http://localhost:1080` | — |
| Keycloak | `http://localhost:9090` | `admin` / `admin` |

> **DB name note**: `docker-compose.yml` creates the database as `book_social_network`, but book service connects to `book_tribe` and feedback service to `book_tribe_feedback` (see `config-server/src/main/resources/config-repo/`). Create both databases manually after the container starts.

### 2. Configure Keycloak

1. Open `http://localhost:9090` → log in as `admin / admin`
2. **Add realm** → import the JSON files from `keycloak/realm/`
3. This creates the `book-social-network` realm and the `bsn` client used by the frontend

### 3. Run the backend services

Start them in this order, each from its own folder and terminal: Config Server and Discovery Server first, because the others read their configuration and register at startup.

```bash
cd config-server && ./mvnw spring-boot:run          # 8888
cd discovery-server && ./mvnw spring-boot:run       # 8761
cd book-tribe-be && ./mvnw spring-boot:run          # 8091
cd feedback-service && ./mvnw spring-boot:run       # 8082
cd notification-service && ./mvnw spring-boot:run   # 8083
cd gateway && ./mvnw spring-boot:run                # 8088
```

On Windows use `mvnw.cmd` instead of `./mvnw`. Feedback service needs a `book_tribe_feedback` database next to `book_tribe`.

The API is served through the gateway on `http://localhost:8088/api/v1/`, and the Eureka dashboard is on `http://localhost:8761`.

**Demo data:** start book service and feedback service with `SEED_DATA=true` (e.g. `SEED_DATA=true ./mvnw spring-boot:run`) to load 12 books, 8 loans and 6 feedbacks from `src/main/resources/seed/*.sql`. Each table is seeded only while it is empty. The books and loans belong to the test users of the realm export: any logged-in user sees the shareable books in the catalogue.

### 4. Run the frontend

```bash
cd book-tribe-ui
npm install
npm start          # ng serve → http://localhost:4200
```

The app redirects unauthenticated users to Keycloak automatically.

## API Docs

Interactive Swagger UI is served by each service on its own port, since the gateway routes only the API paths:

```
http://localhost:8091/api/v1/swagger-ui.html   # book service
http://localhost:8082/api/v1/swagger-ui.html   # feedback service
```

To regenerate the Angular API client after a backend change:

```bash
cd book-tribe-ui
npm run openapi:fetch   # merges the book + feedback specs into src/openapi/openapi.json
npm run api-gen         # reads src/openapi/openapi.json → writes src/app/services/
```

`openapi:fetch` needs book-service and feedback-service running locally.

## Deploy

Free setup, no domain required:

- **Frontend** on Vercel (`book-tribe-ui/`, SPA rewrite in `vercel.json`)
- **Backend + Keycloak** on a home Ubuntu server with `deploy/docker-compose.prod.yml`, exposed over HTTPS with Tailscale Funnel (`https://<host>.ts.net:8443` → gateway, `:10000` → Keycloak)

Step-by-step guide: [deploy/README.md](./deploy/README.md).

## License

[MIT](./LICENSE) © 2024 Giuseppe Pio Ruocco
