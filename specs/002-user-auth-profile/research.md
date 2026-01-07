# Research: User Service Stack & Patterns

**Feature**: User Service (Registration, Auth, Profile)
**Status**: Complete

## Technology Decisions

### 1. Framework & Runtime
- **Decision**: Spring Boot 3 with Java 17+
- **Rationale**: User specified. Spring Boot 3 provides native support for observability, comprehensive security defaults, and a robust ecosystem for microservices.
- **Alternatives**: Micronaut, Quarkus (rejected per user request).

### 2. Security Architecture
- **Decision**: Spring Security + Spring Authorization Server + OAuth2 Client
- **Rationale**:
  - **Spring Security**: Standard for protection (CSRF, CORS, AuthN/AuthZ).
  - **Spring Authorization Server**: For issuing tokens (JWT/Opaque) if acting as an Identity Provider (IdP).
  - **OAuth2 Client**: For social login (Google/GitHub) integration.
- **Implementation Note**: Need to configure a `SecurityFilterChain` that handles:
  - Form login/Basic auth for direct registration.
  - OAuth2 login for social providers.
  - Resource Server configuration to validate JWTs for protected endpoints (Profile).

### 3. Data Persistence
- **Decision**: PostgreSQL + Spring Data JPA + Flyway
- **Rationale**:
  - **PostgreSQL**: Robust relational DB.
  - **Spring Data JPA**: Simplifies repository layer (Repositories vs DAOs).
  - **Flyway**: Version control for DB schema (Constitution requirement for amendments).
- **Optimization**: Use connection pooling (HikariCP - default in Boot 3).

### 4. Object Mapping
- **Decision**: MapStruct + Lombok
- **Rationale**:
  - **Lombok**: Reduces boilerplate (Getters/Setters/Builders).
  - **MapStruct**: High-performance, compile-time DTO-Entity mapping (Constitution II - Performance).
- **Configuration**: Ensure Lombok runs *before* MapStruct in the annotation processor path.

### 5. Testing Strategy
- **Decision**: JUnit 5 + Mockito (Unit/Integration) + Playwright (E2E)
- **Rationale**:
  - **JUnit/Mockito**: Standard Java testing.
  - **Playwright**: Modern, reliable E2E testing for web flows (Registration/Login UI).
- **Constraint**: Constitution requires "Test-First" and explicit "Red-Green-Refactor".

## Best Practices (Constitution Alignment)

- **DTO Usage**: API layer MUST accept/return DTOs, never Entities (BP701).
- **Exception Handling**: Global `@ControllerAdvice` to map custom exceptions (`UserAlreadyExistsException`) to standard HTTP responses (BP302, BP303).
- **Transactions**: `@Transactional` at Service layer. `readOnly=true` for fetch operations (BP602).
- **Security**: No raw SQL. Use JPA Criteria or Repository methods (BP801).

## Implementation Unknowns Resolved

- **Social Login**: Will use standard `oauth2-client` starter. Requires client-id/secret in `application.yaml` (using env vars).
- **Token Management**: Assuming Stateless JWTs for session management to align with "Stateless Services" principle.
