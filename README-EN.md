BookTribe

Project summary:
This monorepo contains a BookTribe application: a Spring Boot backend, an Angular frontend, Keycloak realm export for authentication, and supporting assets (screenshots and uploads). It is intended for local development and integration testing.

Repository layout:
- book-tribe-be/  — Spring Boot backend (Maven)
- book-tribe-ui/  — Angular frontend (Node / Angular CLI)
- keycloak/       — Keycloak realm export and config
- screenshots/    — UI screenshots and diagrams
- uploads/        — Runtime file storage (ignored in VCS)

Quick start (development):
1) Backend: cd book-tribe-be && (Windows) mvnw.cmd spring-boot:run  or (Unix) ./mvnw spring-boot:run
2) Frontend: cd book-tribe-ui && npm install && ng serve
3) Keycloak: import the realm JSON in keycloak/realm into a local Keycloak instance if you need auth

Notes:
- Use the provided Maven and Node wrapper/tooling to match CI.
- Runtime artifacts (target/, uploads/) are environment-specific and typically excluded from source control.
- See subfolder README-EN.md files for per-component details and development hints.
