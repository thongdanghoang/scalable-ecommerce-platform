# Implementation Plan: User Authentication & Profile

**Branch**: `001-user-auth-profile` | **Date**: 2026-01-06 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `specs/001-user-auth-profile/spec.md`

## Summary

Implement a dedicated User Service using Spring Boot to handle user registration, authentication (local + OAuth2), and profile management. This service will serve as the central identity provider for the platform, backed by PostgreSQL.

## Technical Context

**Language/Version**: Java 21 (LTS) [Assumed for modern Spring Boot]
**Primary Dependencies**: Spring Boot 3.x, Spring Security (Auth Server, OAuth2 Client), Spring Web, Hibernate (JPA), Flyway, Lombok, MapStruct
**Storage**: PostgreSQL 16+
**Testing**: JUnit 5, Mockito, Testcontainers (for DB integration tests)
**Target Platform**: Linux (Docker container)
**Project Type**: Microservice (API Backend)
**Performance Goals**: <200ms login response, scalable to 10k concurrent users
**Constraints**: Must handle PII securely, compliant with GDPR/CCPA concepts (data encryption/protection)
**Scale/Scope**: Single microservice, distinct database schema

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Microservices & Independence**: Designed as a standalone 'User Service'.
- [x] **Scalability & Security**: Uses Postgres (scalable), Spring Security (robust).
- [x] **Comprehensive Testing**: Plan includes Unit/Integration/Contract tests.
- [x] **Containerization**: Will be Dockerized.
- [!] **Technology Standards**: User requested **Spring Boot/Java**, which deviates from the Constitution's preferred Node/Python/Go stacks.
  - *Justification*: Spring Security offers robust, industry-standard support for complex Auth flows (OAuth2, OIDC) out-of-the-box which is critical for a User Service.
  - *Status*: **APPROVED** (User explicit request).

## Project Structure

### Documentation (this feature)

```text
specs/001-user-auth-profile/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/userservice/
│   │   │   ├── config/          # Security, Swagger, etc.
│   │   │   ├── controller/      # REST API endpoints
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── mapper/          # MapStruct mappers
│   │   │   ├── repository/      # Spring Data Repositories
│   │   │   └── service/         # Business Logic
│   │   └── resources/
│   │       └── db/migration/    # Flyway scripts
│   └── test/
│       └── java/com/ecommerce/userservice/
│           ├── contract/        # API Contract tests
│           ├── integration/     # DB/Container tests
│           └── unit/            # Service logic tests
├── Dockerfile
└── pom.xml (or build.gradle)
```

**Structure Decision**: Standard Spring Boot Maven/Gradle project layout located in a root-level `user-service/` directory to support the microservices mono-repo structure.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Java/Spring Boot Stack | User explicit request; Robust Auth support | Node/Python require more custom code for OIDC server implementation |
