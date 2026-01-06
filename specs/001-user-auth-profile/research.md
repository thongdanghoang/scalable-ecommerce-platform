# Research: User Authentication & Profile Service

**Feature**: User Authentication & Profile
**Status**: Completed
**Date**: 2026-01-06

## Key Decisions

### 1. Technology Stack
- **Decision**: Java 21 + Spring Boot 3.x
- **Rationale**: User explicitly requested this stack. It provides a mature ecosystem for enterprise-grade authentication and authorization.
- **Alternatives Considered**: Node.js/Express, Python/FastAPI (Standard Constitution stack). Rejected due to specific user requirement and the robust security features of Spring.

### 2. Database & Migration
- **Decision**: PostgreSQL + Flyway
- **Rationale**: PostgreSQL is the constitution standard. Flyway provides version-controlled schema evolution, essential for a service managing critical user data.
- **Implementation**:
  - `V1__init_schema.sql`: Initial user/role tables.
  - `V2__...`: Subsequent changes.

### 3. Authentication Architecture
- **Decision**: Spring Security Authorization Server + OAuth2 Client
- **Rationale**:
  - **Auth Server**: Allows this service to act as the centralized IdP for the entire e-commerce platform, issuing JWTs to other microservices (Order, Cart, etc.).
  - **OAuth2 Client**: Enables "Login with Google/GitHub" functionality, federating identity into our system.
- **Flow**:
  1. User clicks "Login with Google".
  2. Service delegates to Google.
  3. Google returns successful auth.
  4. Service creates/updates local `User` record.
  5. Service issues its own Access Token (JWT) to the frontend/client.

### 4. Data Mapping
- **Decision**: MapStruct + Lombok
- **Rationale**:
  - **Lombok**: Reduces boilerplate (Getters, Setters, Builders) in Entities and DTOs.
  - **MapStruct**: High-performance, compile-time type-safe mapping between Entities and DTOs. Preferred over runtime reflection-based mappers (ModelMapper) for performance.

## Unknowns & Risks

### Resolved Unknowns
- **Java Version**: Assumed Java 21 (LTS) to match modern Spring Boot 3.x baseline.
- **Build Tool**: Will use **Maven** as standard for Spring Boot, unless Gradle is specifically requested later. (Defaulting to Maven for broader familiarity).

### Risks
- **Complexity**: Combining Authorization Server and OAuth2 Client (Social Login) can be complex to configure correctly.
- **Mitigation**: Start with Local Auth (Email/Pass) + JWT issuance. Add Social Login as a second step. Use comprehensive integration tests with Testcontainers to verify flows.
