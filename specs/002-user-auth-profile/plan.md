# Implementation Plan: User Service (Registration, Auth, Profile)

**Branch**: `002-user-auth-profile` | **Date**: 2026-01-06 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-user-auth-profile/spec.md`

## Summary

Implement a scalable User Service using Spring Boot 3 handling Registration, Authentication (Local + OAuth2), and Profile Management. The system will use PostgreSQL for persistence, Spring Security for auth, and expose RESTful APIs defined in OpenAPI.

## Technical Context

**Language/Version**: Java 17/21 (Spring Boot 3 default)
**Primary Dependencies**: 
- Spring Boot 3 Web (REST)
- Spring Security + OAuth2 Client + Authorization Server
- Spring Data JPA
- Flyway (Migration)
- MapStruct (DTO Mapping)
- Lombok
**Storage**: PostgreSQL (Relational)
**Testing**: 
- JUnit 5 + Mockito (Unit/Integration)
- Playwright (E2E)
**Target Platform**: Dockerized Microservice
**Project Type**: Backend Service (Single project structure)
**Performance Goals**: <500ms auth response (SC-002), 100 concurrent logins (SC-003)
**Constraints**: Stateless services, Strict DTO usage (BP701), Secure Password Hashing (BCrypt)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Code Quality**: Will enforce `eclipse-format.xml` and standard Checkstyle.
- **Reliability**: `@Transactional` will be used for business logic boundaries. Exceptions will be mapped to standard error responses.
- **Security**: SQL Injection prevented via JPA. Passwords hashed.
- **Performance**: Stateless JWTs used for session. MapStruct used for efficient mapping.
- **Testing**: Plan includes Unit, Integration, and E2E layers.

## Project Structure

### Documentation (this feature)

```text
specs/002-user-auth-profile/
├── plan.md              # This file
├── research.md          # Technology decisions and best practices
├── data-model.md        # DB Schema and Entity definitions
├── quickstart.md        # Running instructions
├── contracts/           # OpenAPI definitions
│   └── openapi.yaml
└── tasks.md             # Implementation tasks
```

### Source Code (repository root)

```text
code/user-service/
├── build.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/vn/id/thongdanghoang/user_service/
│   │   │   ├── api/          # Controllers (REST)
│   │   │   ├── config/       # SecurityConfig, AppConfig
│   │   │   ├── domain/       # Service Interfaces & Impls
│   │   │   ├── model/        # JPA Entities
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── mapper/       # MapStruct Mappers
│   │   │   ├── repository/   # Spring Data Repositories
│   │   │   └── exception/    # Custom Exceptions & Handlers
│   │   └── resources/
│   │       └── db/migration/ # Flyway SQL scripts
│   └── test/
│       ├── java/.../ut/      # Unit Tests
│       ├── java/.../it/      # Integration Tests
│       └── java/.../e2e/     # Playwright Tests
```

**Structure Decision**: Standard Spring Boot Layered Architecture within the existing `code/user-service` module.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Spring Authorization Server | Need to potentially issue tokens as an IdP later | Simple JWT lib insufficient for OAuth2 standard compliance |