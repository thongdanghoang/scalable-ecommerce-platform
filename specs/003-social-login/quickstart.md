# Quickstart: Social Login

**Feature**: Social Login (Google & GitHub)

## Prerequisites

- Existing `user-db` container (PostgreSQL)
- Google & GitHub OAuth2 Client Credentials

## 1. OAuth2 Configuration

### Google Setup
1. Console: https://console.cloud.google.com/
2. Create Credentials -> OAuth Client ID -> Web Application.
3. Redirect URI: `http://localhost:8080/login/oauth2/code/google`

### GitHub Setup
1. Settings -> Developer Settings -> OAuth Apps.
2. Callback URL: `http://localhost:8080/login/oauth2/code/github`

## 2. Environment Variables

Update your `.env` or `application.yaml` (DO NOT COMMIT):

```bash
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your-google-id
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your-google-secret

export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID=your-github-id
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET=your-github-secret
```

## 3. Running

```bash
# Build
./gradlew clean build -x test

# Run
./gradlew bootRun
```

## 4. Verification

**Browser Flow**:
1. Open `http://localhost:8080/` (or your frontend login page).
2. Click "Login with Google".
3. Verify redirect to Google -> Login -> Redirect back.
4. Check DB: `SELECT * FROM users;` (Should see `provider='GOOGLE'` and `provider_id=...`).
5. Check Profile: `SELECT * FROM user_profiles;` (Should see `avatar_url` populated).

**No Curl Verification**:
OAuth2 requires a browser-based interaction for the Consent Screen. CURL cannot easily simulate this without headless browser automation (e.g., Playwright).
