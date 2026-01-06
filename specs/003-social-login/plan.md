# Implementation Plan: Social Login (Google & GitHub)

**Branch**: `003-social-login` | **Date**: 2026-01-06 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-social-login/spec.md`

## Summary

Implement Social Login (OAuth2) for Google and GitHub, replacing the password-based authentication. The User Service will act as an OIDC Issuer, provisioning local accounts identified by `Provider + ID` and issuing internal JWTs.

## Technical Context

**Language/Version**: Java 17/21 (Spring Boot 3)
**Primary Dependencies**: 
- Spring Boot 3 Web
- Spring Security OAuth2 Client
- Spring Authorization Server (OIDC Issuer)
- Spring Data JPA
- Flyway
**Storage**: PostgreSQL
**Testing**: 
- MockWebServer (IdP simulation)
- Playwright (UI Flow)
**Performance Goals**: OAuth callback processing < 1s (SC-002)
**Constraints**: 
- Disable Password Auth completely.
- Email is optional/nullable.
- No account linking by email.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Code Quality**: Adhere to existing formatting.
- **Reliability**: Use timeouts for external IdP calls.
- **Security**: OAuth2 State parameter enforced. Client Secrets in env vars.
- **Testing**: Test-First (Mocking external IdPs).

## Project Structure

### Documentation (this feature)

```text
specs/003-social-login/
├── plan.md              # This file
├── research.md          # Tech decisions (OAuth2 Client, IdP Behavior)
├── data-model.md        # Schema delta (Composite Key, Nullable Email)
├── quickstart.md        # OAuth setup instructions
├── contracts/           # OpenAPI (Updated)
│   └── openapi.yaml
└── tasks.md             # Implementation tasks
```

### Source Code

```text
code/user-service/
├── src/
│   ├── main/
│   │   ├── java/.../user_service/
│   │   │   ├── config/       # SecurityConfig (OAuth2), WebClientConfig
│   │   │   ├── domain/       # CustomOAuth2UserService (Provisioning Logic)
│   │   │   ├── model/        # User entity updates
│   │   │   └── security/     # Token issuance logic
│   │   └── resources/
│   │       └── db/migration/ # Flyway V2 (Schema updates)
│   └── test/
│       ├── java/.../it/      # OAuth2 Integration Tests
│       └── java/.../e2e/     # Login UI Tests
```

**Structure Decision**: Extending existing User Service structure.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Spring Auth Server (OIDC) | Need to issue *internal* platform tokens after social login | Passing Google tokens creates dependency on Google for all internal service calls |