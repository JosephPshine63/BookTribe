# BookTribe

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen?logo=springboot)
![Angular](https://img.shields.io/badge/Angular-16-red?logo=angular)
![Keycloak](https://img.shields.io/badge/Keycloak-24.0.2-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

A full-stack book-sharing social network where users can lend, borrow, and review books — built as a portfolio project covering a production-grade Spring Boot + Angular + Keycloak architecture.

## Features

- Browse the community catalogue of shareable books (paginated)
- Publish your own books with cover image upload, toggle shareable / archived status
- Borrow a book and return it; owner explicitly approves the return
- Leave a star rating and written feedback on any book you have borrowed
- Register with email verification via a 6-digit OTP (delivered through MailDev in dev)
- All authentication and authorisation delegated to Keycloak (JWT / OAuth2)

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2.2 · Java 17 · Spring Security OAuth2 Resource Server |
| Database | PostgreSQL · JPA / Hibernate (`ddl-auto: update`) |
| Auth | Keycloak 24.0.2 — realm `book-social-network`, client `bsn` |
| Mail (dev) | MailDev |
| Frontend | Angular 16 · Bootstrap 5 · keycloak-js |
| API contract | OpenAPI 3 (Springdoc) · `ng-openapi-gen` |

## Architecture

The backend is a single Spring Boot application (`/api/v1/`) structured around three domain modules — **Book**, **Feedback**, and **History** — each with its own controller → service → repository stack. A `BaseEntity` superclass provides JPA auditing fields on every entity. All authentication is handled externally by Keycloak; the backend validates JWTs as an OAuth2 resource server and maps Keycloak roles to Spring Security authorities via `KeycloakJwtAuthenticationConverter`.

The Angular frontend consumes the backend through a fully generated API client (`ng-openapi-gen`) that is regenerated from the OpenAPI spec whenever the backend contract changes. Keycloak-js handles the auth flow (`login-required` on app init) and an `HttpTokenInterceptor` attaches the Bearer token to every request.

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

> **DB name note**: `docker-compose.yml` creates the database as `book_social_network`, but `application-dev.yml` connects to `book_tribe`. Either create a `book_tribe` database manually after the container starts, or change the `POSTGRES_DB` value in `docker-compose.yml` to `book_tribe`.

### 2. Configure Keycloak

1. Open `http://localhost:9090` → log in as `admin / admin`
2. **Add realm** → import the JSON files from `keycloak/realm/`
3. This creates the `book-social-network` realm and the `bsn` client used by the frontend

### 3. Run the backend

```bash
cd book-tribe-be

# Unix
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8088/api/v1/`.

### 4. Run the frontend

```bash
cd book-tribe-ui
npm install
npm start          # ng serve → http://localhost:4200
```

The app redirects unauthenticated users to Keycloak automatically.

## API Docs

Interactive Swagger UI is available at:

```
http://localhost:8088/api/v1/swagger-ui.html
```

To regenerate the Angular API client after a backend change:

```bash
cd book-tribe-ui
npm run api-gen    # reads src/openapi/openapi.json → writes src/app/services/
```

## License

[MIT](./LICENSE) © 2024 Giuseppe Pio Ruocco
