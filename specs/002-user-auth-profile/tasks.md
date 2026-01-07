---
description: "Task list template for feature implementation"
---

# Tasks: User Service (Registration, Auth, Profile)

**Input**: Design documents from `/specs/002-user-auth-profile/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are INCLUDED as per "Test-First" Constitution Principle (III).

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Project Root**: `code/user-service/`
- **Source**: `code/user-service/src/main/java/vn/id/thongdanghoang/user_service/`
- **Tests**: `code/user-service/src/test/java/vn/id/thongdanghoang/user_service/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Configure build.gradle.kts with Spring Boot 3, Security, JPA, Flyway, MapStruct, Lombok dependencies in code/user-service/build.gradle.kts
- [x] T002 [P] Configure Checkstyle and Eclipse Formatter in code/user-service/config/checkstyle/checkstyle.xml and eclipse-format.xml
- [x] T003 Configure Docker Compose for PostgreSQL in code/user-service/docker-compose.yml
- [x] T004 Configure application.yml with Datasource and Flyway settings in code/user-service/src/main/resources/application.yaml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T005 Create BaseEntity with auditing in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/model/BaseEntity.java
- [ ] T006 Create User and UserProfile Entities in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/model/
- [x] T007 Create Flyway V1 migration script for users and profiles tables in code/user-service/src/main/resources/db/migration/V1__init_schema.sql
- [ ] T008 [P] Implement Global Exception Handler and ErrorResponse DTO in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/exception/GlobalExceptionHandler.java
- [x] T009 Set up SecurityConfig skeleton (PasswordEncoder, SecurityFilterChain) in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/SecurityConfig.java

**Checkpoint**: Foundation ready - DB accessible, Security basic config in place.

---

## Phase 3: User Story 1 - User Registration (Priority: P1)

**Goal**: Allow users to sign up with email/password.

**Independent Test**: Contract test for /auth/register and Integration test for successful registration.

### Tests for User Story 1 ⚠️

- [ ] T010 [P] [US1] Create Contract Test for registration endpoint in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/contract/AuthContractTest.java

### Implementation for User Story 1

- [ ] T011 [P] [US1] Create RegistrationRequest and AuthResponse DTOs in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/dto/
- [ ] T012 [P] [US1] Create UserRepository interface in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/repository/UserRepository.java
- [ ] T013 [P] [US1] Create UserMapper using MapStruct in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/mapper/UserMapper.java
- [ ] T014 [US1] Implement UserService.registerUser logic (hashing, saving) in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/domain/UserService.java
- [ ] T015 [US1] Implement AuthController.register endpoint in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/api/AuthController.java
- [ ] T016 [US1] Create Integration Test for Registration Flow in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/it/RegistrationIntegrationTest.java

**Checkpoint**: User can register, data is persisted in DB, password is hashed.

---

## Phase 4: User Story 2 - User Authentication (Priority: P1)

**Goal**: Allow users to login and receive JWT.

**Independent Test**: E2E Login Flow.

### Tests for User Story 2 ⚠️

- [ ] T017 [P] [US2] Create Contract Test for login endpoint in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/contract/AuthContractTest.java

### Implementation for User Story 2

- [ ] T018 [P] [US2] Create LoginRequest DTO in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/dto/LoginRequest.java
- [ ] T019 [US2] Implement JwtService for token generation/validation in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/JwtService.java
- [ ] T020 [US2] Implement UserService.authenticate logic in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/domain/UserService.java
- [ ] T021 [US2] Implement AuthController.login endpoint in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/api/AuthController.java
- [ ] T022 [US2] Update SecurityConfig to add JwtFilter and AuthManager in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/SecurityConfig.java
- [ ] T023 [US2] Create Playwright E2E Test for Login in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/e2e/LoginE2ETest.java

**Checkpoint**: User can login, receive JWT, and JWT is valid.

---

## Phase 5: User Story 3 - Profile Management (Priority: P2)

**Goal**: View and update user profile.

**Independent Test**: Integration test for Profile update.

### Tests for User Story 3 ⚠️

- [ ] T024 [P] [US3] Create Contract Test for /users/me endpoints in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/contract/ProfileContractTest.java

### Implementation for User Story 3

- [ ] T025 [P] [US3] Create UserProfileResponse and UpdateProfileRequest DTOs in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/dto/
- [ ] T026 [P] [US3] Create UserProfileRepository in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/repository/UserProfileRepository.java
- [ ] T027 [US3] Implement UserService.getProfile/updateProfile in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/domain/UserService.java
- [ ] T028 [US3] Implement ProfileController endpoints in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/api/ProfileController.java
- [ ] T029 [US3] Update SecurityConfig to secure /users/** endpoints in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/SecurityConfig.java
- [ ] T030 [US3] Create Integration Test for Profile Operations in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/it/ProfileIntegrationTest.java

**Checkpoint**: User can view/update profile using JWT.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T031 Update README.md with API documentation and setup steps
- [ ] T032 Run static analysis (Checkstyle) and fix violations

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies.
- **Foundational (Phase 2)**: Depends on Setup. BLOCKS all stories.
- **US1 (Registration)**: Depends on Foundation.
- **US2 (Auth)**: Depends on Foundation + US1 (need registered user to login).
- **US3 (Profile)**: Depends on Foundation + US2 (need auth to access profile).

### Parallel Opportunities

- **Phase 1**: T001, T002, T003 can run in parallel.
- **Phase 2**: T008 (Exceptions) and T009 (Security) are largely independent of DB tasks.
- **US1**: T011 (DTOs), T012 (Mapper), T013 (Repository) can start in parallel.
- **US2**: T018 (DTO) and T019 (JwtService) can run in parallel.

## Implementation Strategy

### MVP First (US1 + US2)

1. Complete Setup & Foundation.
2. Implement Registration (US1) -> Verify with Contract/IT tests.
3. Implement Auth (US2) -> Verify with E2E tests.
4. **Deploy MVP**: User can register and login.

### Incremental Delivery (US3)

1. Add Profile Management (US3).
2. Verify with Integration tests.
3. **Release v1.1**: Adds profile features.
