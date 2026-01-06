---
description: "Task list for User Authentication & Profile feature"
---

# Tasks: User Authentication & Profile

**Input**: Design documents from `specs/001-user-auth-profile/`
**Prerequisites**: plan.md, spec.md, data-model.md, contracts/

**Tests**: Tests are included as per the implementation plan's constitution check.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create Spring Boot project structure in `user-service/` with Maven
- [ ] T002 Update `user-service/pom.xml` with dependencies (Spring Web, Security, JPA, Flyway, Postgres, Lombok, MapStruct)
- [ ] T003 [P] Create `user-service/Dockerfile` for containerization
- [ ] T004 [P] Create `docker-compose.yml` (at root) for PostgreSQL database
- [ ] T005 Configure `user-service/src/main/resources/application.yml` (DB connection, JPA settings)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T006 Create Flyway migration `V1__init_schema.sql` in `user-service/src/main/resources/db/migration/`
- [ ] T007 [P] Create `User` entity in `user-service/src/main/java/com/ecommerce/userservice/entity/User.java`
- [ ] T008 [P] Create `UserRepository` interface in `user-service/src/main/java/com/ecommerce/userservice/repository/UserRepository.java`
- [ ] T009 Implement Global Exception Handler in `user-service/src/main/java/com/ecommerce/userservice/exception/GlobalExceptionHandler.java`
- [ ] T010 Setup basic `SecurityConfig` (permit all for now) in `user-service/src/main/java/com/ecommerce/userservice/config/SecurityConfig.java`

**Checkpoint**: Foundation ready - Database connects, app starts, schema migrates.

---

## Phase 3: User Story 1 - User Registration (Priority: P1) 🎯 MVP

**Goal**: Users can sign up with email/password.
**Independent Test**: Call `POST /api/v1/auth/register` and verify 201 Created + User in DB.

### Tests for User Story 1
- [ ] T011 [P] [US1] Create integration test for registration in `user-service/src/test/java/com/ecommerce/userservice/integration/RegistrationIntegrationTest.java`

### Implementation for User Story 1
- [ ] T012 [P] [US1] Create `RegistrationRequest` DTO in `user-service/src/main/java/com/ecommerce/userservice/dto/RegistrationRequest.java`
- [ ] T013 [P] [US1] Create `AuthResponse` DTO in `user-service/src/main/java/com/ecommerce/userservice/dto/AuthResponse.java`
- [ ] T014 [US1] Create `UserMapper` in `user-service/src/main/java/com/ecommerce/userservice/mapper/UserMapper.java`
- [ ] T015 [US1] Implement `UserService.registerUser` logic in `user-service/src/main/java/com/ecommerce/userservice/service/UserService.java`
- [ ] T016 [US1] Implement `AuthController.register` endpoint in `user-service/src/main/java/com/ecommerce/userservice/controller/AuthController.java`
- [ ] T017 [US1] Configure `PasswordEncoder` bean in `user-service/src/main/java/com/ecommerce/userservice/config/AppConfig.java`

**Checkpoint**: Registration works. User is saved with hashed password.

---

## Phase 4: User Story 2 - User Login & Logout (Priority: P1)

**Goal**: Users can login to receive a JWT and logout.
**Independent Test**: Call `POST /api/v1/auth/login` → receive JWT. Call protected route with JWT → Success.

### Tests for User Story 2
- [ ] T018 [P] [US2] Create integration test for login in `user-service/src/test/java/com/ecommerce/userservice/integration/LoginIntegrationTest.java`

### Implementation for User Story 2
- [ ] T019 [P] [US2] Create `LoginRequest` DTO in `user-service/src/main/java/com/ecommerce/userservice/dto/LoginRequest.java`
- [ ] T020 [P] [US2] Implement `JwtUtil` for token generation/validation in `user-service/src/main/java/com/ecommerce/userservice/security/JwtUtil.java`
- [ ] T021 [US2] Implement `CustomUserDetailsService` in `user-service/src/main/java/com/ecommerce/userservice/security/CustomUserDetailsService.java`
- [ ] T022 [US2] Update `SecurityConfig` to expose `AuthenticationManager` bean
- [ ] T023 [US2] Implement `UserService.login` logic in `user-service/src/main/java/com/ecommerce/userservice/service/UserService.java`
- [ ] T024 [US2] Implement `AuthController.login` endpoint in `user-service/src/main/java/com/ecommerce/userservice/controller/AuthController.java`

**Checkpoint**: Login returns valid JWT.

---

## Phase 5: User Story 3 - Profile Management (Priority: P2)

**Goal**: Authenticated users can view and update their profile.
**Independent Test**: Login → Get JWT → `GET /users/me` → Returns profile. `PUT /users/me` → Updates name.

### Tests for User Story 3
- [ ] T025 [P] [US3] Create integration test for profile operations in `user-service/src/test/java/com/ecommerce/userservice/integration/ProfileIntegrationTest.java`

### Implementation for User Story 3
- [ ] T026 [P] [US3] Implement `JwtAuthenticationFilter` in `user-service/src/main/java/com/ecommerce/userservice/security/JwtAuthenticationFilter.java`
- [ ] T027 [US3] Update `SecurityConfig` to add JWT filter and secure `/api/v1/users/**` endpoints
- [ ] T028 [P] [US3] Create `UserProfile` and `UpdateProfileRequest` DTOs in `user-service/src/main/java/com/ecommerce/userservice/dto/`
- [ ] T029 [P] [US3] Create `ChangePasswordRequest` DTO in `user-service/src/main/java/com/ecommerce/userservice/dto/ChangePasswordRequest.java`
- [ ] T030 [US3] Implement `UserService.getUserProfile` and `updateUserProfile` logic
- [ ] T031 [US3] Implement `UserService.changePassword` logic
- [ ] T032 [US3] Implement `UserController` with `me`, `update`, `changePassword` endpoints in `user-service/src/main/java/com/ecommerce/userservice/controller/UserController.java`

**Checkpoint**: Protected endpoints require JWT. Profile data is accessible and mutable.

---

## Phase 6: User Story 4 - Password Recovery (Priority: P3)

**Goal**: Users can reset password via email (simulated).
**Independent Test**: Request reset → Mock email log → Use token to set new password → Login with new password.

### Tests for User Story 4
- [ ] T033 [P] [US4] Create integration test for recovery flow in `user-service/src/test/java/com/ecommerce/userservice/integration/RecoveryIntegrationTest.java`

### Implementation for User Story 4
- [ ] T034 [P] [US4] Create `PasswordResetToken` entity (or redis support) - using simple DB table `password_reset_tokens` via new Flyway script `V2__reset_tokens.sql`
- [ ] T035 [US4] Implement `EmailService` (Mock implementation logging to console) in `user-service/src/main/java/com/ecommerce/userservice/service/EmailService.java`
- [ ] T036 [US4] Implement `UserService.initiatePasswordReset` and `completePasswordReset` logic
- [ ] T037 [US4] Add `AuthController` endpoints for `/forgot-password` and `/reset-password`

**Checkpoint**: Full recovery flow works (with mocked email).

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T038 [P] Add OpenAPI/Swagger configuration in `user-service/src/main/java/com/ecommerce/userservice/config/OpenApiConfig.java`
- [ ] T039 Verify all tests pass with `./mvnw verify`
- [ ] T040 Update `specs/001-user-auth-profile/quickstart.md` with final API details

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: No dependencies.
- **Foundational (Phase 2)**: Depends on Phase 1. Blocks all stories.
- **US1 (Phase 3)**: Depends on Phase 2.
- **US2 (Phase 4)**: Depends on Phase 2 (and effectively Phase 3 for creating users to login).
- **US3 (Phase 5)**: Depends on Phase 4 (needs Auth/JWT infrastructure).
- **US4 (Phase 6)**: Depends on Phase 2 (independent of other stories technically, but logically last).

### Parallel Opportunities
- Models and DTOs can often be created in parallel with Test definitions.
- `UserController` (US3) can be scaffolded while `AuthController` (US2) is being finished.

## Implementation Strategy

1. **Setup & Foundation**: Build the shell and database connection.
2. **MVP (US1 + US2)**: Focus on Register -> Login loop. This is the critical path.
3. **Enhancement (US3)**: Add the "authenticated" experience.
4. **Support (US4)**: Add recovery flows.
