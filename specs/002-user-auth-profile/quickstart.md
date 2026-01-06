# Quickstart: User Service

**Feature**: User Service (Registration, Auth, Profile)

## Prerequisites

- Java 17 or 21
- Docker (for Postgres)
- Maven or Gradle (Wrapper included)

## 1. Environment Setup

Start the database container:
```bash
docker run --name user-db -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=userdb -p 5432:5432 -d postgres:15
```

Set environment variables (or update `application.yaml`):
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/userdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=secret
# For OAuth2 (Optional for local test if just using Form Login)
export GOOGLE_CLIENT_ID=xxx
export GOOGLE_CLIENT_SECRET=xxx
```

## 2. Build & Run

```bash
# Build
./gradlew clean build -x test

# Run
./gradlew bootRun
```

## 3. Manual Verification (cURL)

**Register a User**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com", "password":"StrongPassword123!"}'
```

**Login**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com", "password":"StrongPassword123!"}'
# Copy the token from response
```

**Get Profile**:
```bash
TOKEN="<paste_token_here>"
curl http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer $TOKEN"
```

## 4. Running Tests

**Unit & Integration Tests**:
```bash
./gradlew test
```

**E2E Tests (Playwright)**:

```bash
# Ensure app is running
npm install # inside e2e directory
npx playwright test
```
