# Feature Specification: Social Login (Google & GitHub)

**Feature Branch**: `003-social-login`  
**Created**: 2026-01-06  
**Status**: Draft  
**Input**: User description: "Implement social login functionality using Google and GitHub providers"

## Clarifications

### Session 2026-01-06

- Q: Auth Method & Password Fallback: Does this mean we must remove/disable the password-based registration and login
  features, enforcing "Social Login Only" for all users? → A: Yes, enforce Social Login Only (Disable Password Auth)
- Q: Identity Provider (IdP) Behavior: Should the User Service act as a full OIDC Issuer or pass through social
  tokens? → A: Internal IdP (OIDC Issuer)
- Q: Missing Social Email Handling: How should the system handle social logins that do not return a valid/verified real
  email address? → A: Use Provider Name + Subject ID as primary identity key; sync Email to profile only if
  available/public, otherwise leave empty.
- Q: Account Linking across Providers: Since we now key users by Provider + ID, should we attempt to automatically link
  accounts if emails match? → A: No, keep separate accounts
- Q: Additional Profile Data Sync: Besides Email, what other profile attributes should be automatically synchronized
  from the Social Provider to the local User Profile upon login? → A: Standard OIDC Attributes (Name, Avatar, Locale)

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Login with Google (Priority: P1)

As a visitor, I want to log in using my Google account so that I can access the platform without creating a new
password.

**Why this priority**: High user demand for Google login convenience; reduces barrier to entry.

**Independent Test**: Can be tested by clicking "Login with Google", authenticating on Google's mock/sandbox page, and
verifying a valid session is created in the app.

**Acceptance Scenarios**:

1. **Given** a visitor on the login page, **When** they click "Login with Google" and authorize the app, **Then** they
   are redirected back and logged in successfully.
2. **Given** a new user using Google login, **When** they authenticate for the first time, **Then** a new local account
   is automatically created using their Google Subject ID.
3. **Given** a user who denies permissions on the Google consent screen, **When** they are redirected back, **Then**
   they see a "Login Cancelled" message and remain on the login page.

---

### User Story 2 - Login with GitHub (Priority: P1)

As a developer user, I want to log in using my GitHub account so that I can use my existing dev credentials.

**Why this priority**: Key demographic for technical platforms; alternative to Google.

**Independent Test**: Can be tested by clicking "Login with GitHub", authenticating on GitHub, and verifying session
creation.

**Acceptance Scenarios**:

1. **Given** a visitor on the login page, **When** they click "Login with GitHub" and authorize, **Then** they are
   logged in.
2. **Given** a user with a private GitHub email, **When** they login, **Then** their account is created with an empty
   email field, keyed by their GitHub ID.

### Edge Cases

- What happens if the email from the social provider is missing (e.g., private GitHub email)? (Sync empty email to
  profile; account created successfully via Provider ID)
- What happens if the social provider service is down? (Show "Service Unavailable" message; user must try another
  provider or wait)
- What happens if a user tries to link a social account already linked to another user? (Reject with error)
- What happens if a user logs in with Google and GitHub using the same email? (Two separate accounts will be created, as
  accounts are keyed by Provider + ID).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST support OAuth2 Authorization Code flow for Google Identity.
- **FR-002**: System MUST support OAuth2 Authorization Code flow for GitHub.
- **FR-003**: System MUST automatically provision a new User account if the Provider ID + Provider Name combination does
  not exist.
- **FR-004**: System MUST store the Auth Provider type (GOOGLE, GITHUB) and Provider ID as the primary unique identity
  for the user.
- **FR-005**: System MUST disable and remove local password-based authentication and registration.
- **FR-006**: System MUST sync the user's email address to their User Profile only if provided/public by the IdP;
  otherwise, it MUST be left empty.
- **FR-007**: System MUST handle OAuth2 error callbacks (e.g., access_denied) gracefully.
- **FR-008**: System MUST act as an OIDC Issuer, generating platform-specific JWTs for authenticated sessions, not
  forwarding external tokens.
- **FR-009**: System MUST NOT use Email Address as the primary key for authentication matching or account linking.
- **FR-010**: System MUST treat each Provider + Provider ID combination as a distinct, independent account.
- **FR-011**: System MUST synchronize standard profile attributes (First Name, Last Name, Avatar URL, Locale) from the
  Social Provider to the local User Profile during authentication, applying the parsing logic defined in the Data Model.

### Key Entities

- **User**: Updated to include `auth_provider` (ENUM) and `provider_id` (String) as a composite unique key. `email`
  becomes nullable/optional. `password_hash` removed.
- **OAuth2Session**: Temporary state for the authentication flow (managed by framework/library).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can complete a social login flow in under 5 clicks.
- **SC-002**: 99% of successful OAuth callbacks result in a valid user session within 1 second.
- **SC-003**: System supports creating accounts without email addresses 100% of the time.
