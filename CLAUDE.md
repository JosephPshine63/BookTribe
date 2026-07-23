# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

Monorepo containing three independent components:

- `book-tribe-be/` — Spring Boot 3.2.2 backend (Java 17, Maven)
- `book-tribe-ui/` — Angular 16 frontend (Node / Angular CLI)
- `keycloak/` — Keycloak realm export (`book-social-network` realm, `bsn` client)
- `docker-compose.yml` — local infrastructure (Postgres, MailDev, Keycloak)

## Infrastructure (docker-compose)

Start all dependencies before running either app:

```bash
docker compose up -d
```

| Service  | Port(s)   | Credentials         |
|----------|-----------|---------------------|
| Postgres | 5432      | username / password |
| MailDev  | 1080 (UI), 1025 (SMTP) | —      |
| Keycloak | 9090      | admin / admin       |

> **Note**: `docker-compose.yml` creates the DB as `book_social_network`, but `application-dev.yml` connects to `book_tribe`. You may need to create the `book_tribe` database manually or align these names.

After starting Keycloak, import the realm from `keycloak/realm/book-social-network` (a realm-export JSON, despite the extensionless filename) to create the `book-social-network` realm and `bsn` client.

> **Note**: `application-dev.yml`'s `issuer-uri` points at realm `book-tribe`, but the importable realm export (and the frontend's `keycloak.service.ts`) both use `book-social-network`. Align these — either edit the export/frontend to `book-tribe`, or fix the issuer-uri to `book-social-network` — before expecting login to work end-to-end.

## Backend (`book-tribe-be/`)

### Commands

```bash
# Run (dev profile active by default)
./mvnw spring-boot:run

# Build JAR
./mvnw package

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=BookTribeApplicationTests
```

- API base path: `http://localhost:8088/api/v1/`
- Swagger UI: `http://localhost:8088/api/v1/swagger-ui.html`

### Architecture

Package root: `dev.pioruocco.book`

| Package     | Responsibility |
|-------------|----------------|
| `book`      | Core domain — CRUD, borrow/return/approve flow, cover upload |
| `feedback`  | Star ratings on books |
| `history`   | `BookTransactionHistory` — tracks borrow/return events |
| `user`      | `User` entity (implements `UserDetails`/`Principal`), `Token` for email activation |
| `role`      | `Role` entity (many-to-many with User) |
| `file`      | `FileStorageService` — saves cover images to `./uploads` |
| `email`     | Thymeleaf-based activation email via SMTP |
| `security`  | OAuth2 resource server config + Keycloak JWT converter |
| `config`    | CORS/beans, OpenAPI, JPA auditing aware |
| `handler`   | `GlobalExceptionHandler` + `BusinessErrorCodes` |
| `exception` | Domain exceptions (`ActivationTokenException`, `OperationNotPermittedException`) |
| `common`    | `BaseEntity` (audited superclass), `PageResponse<T>` |

**Auth model**: Authentication is fully delegated to Keycloak. The `User` entity has `@Entity`/`@Table` commented out — it is not persisted to the DB. Authenticated requests arrive as Keycloak JWTs; `KeycloakJwtAuthenticationConverter` extracts roles from the `resource_access.account.roles` JWT claim and maps them to `ROLE_*` granted authorities. The `/auth/**` endpoints and Swagger paths are public; everything else requires a valid JWT.

**Auditing**: `BaseEntity` uses Spring Data JPA auditing (`@CreatedBy`, `@CreatedDate`, etc.). `ApplicationAuditAware` provides the current user's email for audit fields.

**Borrow flow**: `Book` → `BookTransactionHistory` — a user borrows a book (creates a history entry), returns it (sets `returned = true`), owner approves return (sets `returnApproved = true`).

### Patterns

- Controllers receive `Authentication connectedUser` for the current Keycloak principal.
- `*Request` / `*Response` DTOs mapped via `*Mapper` classes (no MapStruct — manual mapping).
- `BookSpecification` provides JPA Criteria API filtering for the book list.
- All paginated endpoints use the shared `PageResponse<T>` wrapper.

## Frontend (`book-tribe-ui/`)

### Commands

```bash
cd book-tribe-ui

npm install        # first-time setup
npm start          # ng serve → http://localhost:4200
npm test           # Karma/Jasmine unit tests
npm run build      # production build

# Regenerate API client from OpenAPI spec
npm run api-gen    # reads src/openapi/openapi.json, writes src/app/services/
```

### Architecture

```
src/app/
  pages/            # Auth pages (login, register, activate-account) — not lazy-loaded
  modules/book/     # Main feature module — lazy-loaded, auth-guarded
    pages/          # Full-page views (book-list, my-books, borrowed, returned, manage, details)
    components/     # Reusable UI (book-card, menu, rating)
  services/
    services/       # Generated Angular services (authentication, book, feedback)
    models/         # Generated TS model interfaces
    fn/             # Generated per-operation functions (used internally by services)
    keycloak/       # KeycloakService — wraps keycloak-js adapter
    interceptor/    # HttpTokenInterceptor — injects Bearer token from Keycloak on all requests
    guard/          # authGuard — redirects to Keycloak login if not authenticated
    token/          # TokenService (legacy JWT helper, may be unused with Keycloak path)
```

**Auth flow**: On app init, `KeycloakService.init()` triggers `login-required` — unauthenticated users are redirected to Keycloak at `http://localhost:9090` (realm `book-social-network`, client `bsn`). After login the Keycloak token is attached to every HTTP request by `HttpTokenInterceptor`.

**API client**: `src/app/services/` is fully auto-generated by `ng-openapi-gen` from `src/openapi/openapi.json`. Do not hand-edit files inside `services/fn/`, `services/models/`, or `services/services/` — regenerate with `npm run api-gen` after changing the backend API.
