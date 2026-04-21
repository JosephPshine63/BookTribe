Book Tribe - Backend (English)

Overview:
This folder contains the Spring Boot backend for the Book Social Network project. It is a Maven-based Java application that implements the REST API, business logic, and data persistence used by the frontend.

How to run (development):
- Windows: mvnw.cmd spring-boot:run
- Unix: ./mvnw spring-boot:run

Key files and folders:
- pom.xml - Maven project file and dependencies
- mvnw / mvnw.cmd - Maven wrapper (use these to match CI)
- src/ - Java source code (controllers, services, repositories, domain models)
- target/ - Build output (ignored in VCS)

Notes:
- Use the provided Maven wrapper. See top-level README for full integration instructions (frontend, Keycloak, docker-compose).