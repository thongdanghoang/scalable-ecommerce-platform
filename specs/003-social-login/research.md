# Research: Social Login (Google & GitHub)

**Feature**: Social Login (Google & GitHub)
**Status**: Complete

## Technology Decisions

### 1. Framework & Security
- **Decision**: Spring Security OAuth2 Client + Spring Authorization Server
- **Rationale**: 
  - **OAuth2 Client**: Native Spring Boot support for "Login with Google/GitHub". Handles the authorization code flow, token exchange, and user info fetching automatically.
  - **Spring Authorization Server**: As decided in Clarifications, the User Service acts as an OIDC Issuer. It will mint its *own* JWTs for the session after validating the social login, rather than passing through the Google/GitHub tokens.
- **Alternatives**: Keycloak (Too heavy for just 2 providers + strict "code-first" constitution), Auth0 (Cost/Vendor lock-in).

### 2. Identity Model Changes
- **Decision**: Composite Key (`provider` + `provider_id`)
- **Rationale**: Emails are no longer guaranteed unique or present (GitHub privacy). `provider` (ENUM) + `provider_id` (String) will strictly identify a user account.
- **Migration**: Existing `email` column becomes nullable and non-unique index (or secondary index).

### 3. Testing Strategy
- **Decision**: MockWebServer for IdP responses + Playwright for UI flow
- **Rationale**: 
  - **Unit/Integration**: Use `MockWebServer` (or WireMock) to simulate Google/GitHub token and user-info endpoints. This ensures tests don't rely on real external network calls (Constitution II - Reliability).
  - **E2E**: Playwright will interact with the "Login with X" buttons. (Real social login testing in CI is brittle/blocked; will use a test-mode flag or mock IdP for E2E).

## Best Practices (Constitution Alignment)

- **Reliability**: External IdP calls must have timeouts and circuit breakers (Resilience4j) to handle downtime (FR-007).
- **Security**: 
  - `client-id` and `client-secret` MUST be loaded from environment variables, never committed.
  - State parameter MUST be used in OAuth2 flow to prevent CSRF (Spring Security handles this by default).
- **Data Integrity**: Profile sync (Name, Avatar) should happen strictly *after* successful authentication (BP602 - Transactional).

## Implementation Unknowns Resolved

- **Email Handling**: Email is strictly "info-only" and synced to profile if available. It is NOT the login handle anymore.
- **Account Linking**: Explicitly disabled. User logging in with Google vs GitHub creates two separate accounts.
