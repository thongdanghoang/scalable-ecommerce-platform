---
description: "Task list template for feature implementation"
---

# Tasks: Social Login (Google & GitHub)

**Input**: Design documents from `/specs/003-social-login/`
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

- [x] T001 Configure Spring Security OAuth2 Client dependency in code/user-service/build.gradle.kts
- [x] T002 [P] Update application.yml with Google/GitHub client placeholders in code/user-service/src/main/resources/application.yaml
- [x] T003 [P] Create AuthProvider Enum (Cancelled: using String providerName)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T004 Create Flyway V2 migration to update users table (Merged into V1) in code/user-service/src/main/resources/db/migration/V1__init_schema.sql
- [x] T005 Update User Entity with provider composite key in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/model/User.java
- [ ] T006 Update UserProfile Entity with avatarUrl and locale fields in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/model/UserProfile.java
- [x] T007 [P] Create CustomOAuth2User model (Replaced by FederatedIdentityTokenCustomizer logic)
- [x] T008 [P] Implement CustomOAuth2UserService (Replaced by FederatedIdentityTokenCustomizer logic)

**Checkpoint**: DB schema updated, basic OAuth2 user mapping logic ready.

---

## Phase 3: User Story 1 - Login with Google (Priority: P1)

**Goal**: Enable Google Login, create account from Google ID.

**Independent Test**: Integration test with MockWebServer simulating Google.

### Tests for User Story 1 ⚠️

- [ ] T009 [P] [US1] Create OAuth2 Integration Test for Google Login flow in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/it/GoogleOAuth2IntegrationTest.java

### Implementation for User Story 1

- [x] T010 [US1] Update SecurityConfig to enable oauth2Login() with Google registration in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/SecurityConfig.java
- [ ] T011 [US1] Implement GoogleUserInfoMapper to map attributes to User entity (Pending integration with TokenCustomizer)
- [x] T012 [US1] Update CustomOAuth2UserService (Replaced by FederatedIdentityTokenCustomizer)
- [ ] T013 [US1] Implement SuccessHandler to issue internal JWT after Google login (Handled by Auth Server flow)

**Checkpoint**: User can login via Google, account created, JWT issued.

---

## Phase 4: User Story 2 - Login with GitHub (Priority: P1)

**Goal**: Enable GitHub Login, handle missing email.

**Independent Test**: Integration test with MockWebServer simulating GitHub (private email scenario).

### Tests for User Story 2 ⚠️

- [ ] T014 [P] [US2] Create OAuth2 Integration Test for GitHub Login flow in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/it/GitHubOAuth2IntegrationTest.java

### Implementation for User Story 2

- [x] T015 [US2] Update SecurityConfig to add GitHub registration in code/user-service/src/main/java/vn/id/thongdanghoang/user_service/config/SecurityConfig.java
- [ ] T016 [US2] Implement GitHubUserInfoMapper to map attributes (handle null email) (Pending integration with TokenCustomizer)
- [x] T017 [US2] Update CustomOAuth2UserService (Replaced by FederatedIdentityTokenCustomizer)
- [ ] T018 [P] [US2] Configure Keycloak realm (realm.json) and Testcontainers setup for E2E tests in code/user-service/src/test/resources/keycloak/realm.json
- [ ] T019 [US2] Create Playwright E2E Test using Testcontainers and Keycloak to simulate GitHub login in code/user-service/src/test/java/vn/id/thongdanghoang/user_service/e2e/SocialLoginE2ETest.java

**Checkpoint**: User can login via GitHub, account created even if email is missing.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T020 Clean up legacy password authentication code (UserService, AuthController)
- [ ] T021 Update README.md with OAuth2 setup instructions

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies.
- **Foundational (Phase 2)**: Depends on Setup. BLOCKS all stories.
- **US1 (Google)**: Depends on Foundation.
- **US2 (GitHub)**: Depends on Foundation. (Independent of US1).

### Parallel Opportunities

- **Phase 1**: T001, T002, T003 can run in parallel.
- **Phase 2**: T007 (Model) and T008 (Service) can run in parallel with T004/T005 (DB/Entity).
- **US1 vs US2**: Can be implemented in parallel after Foundation.

## Implementation Strategy

### MVP First (US1)

1. Setup OAuth2 Client.
2. Implement DB changes (Schema V2).
3. Implement Google Login (US1).
4. Verify JWT issuance.

### Incremental Delivery (US2)

1. Add GitHub support (US2).
2. Handle "No Email" edge case.
3. Remove legacy password code.
