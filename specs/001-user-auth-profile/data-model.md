# Data Model: User Service

## Entities

### User
Represents a registered user in the system.

| Field | Type | Required | Unique | Description |
|-------|------|----------|--------|-------------|
| `id` | UUID | Yes | Yes | Primary Key. |
| `email` | VARCHAR(255) | Yes | Yes | User's email address. Used for login. |
| `password_hash` | VARCHAR(255) | No | No | BCrypt encoded password. Null for social-only users. |
| `full_name` | VARCHAR(100) | Yes | No | User's display name. |
| `provider` | VARCHAR(20) | Yes | No | Auth provider: `LOCAL`, `GOOGLE`, `GITHUB`. |
| `provider_id` | VARCHAR(255) | No | No | Unique ID from external provider (if applicable). |
| `role` | VARCHAR(20) | Yes | No | User role: `ROLE_USER`, `ROLE_ADMIN`. Default `ROLE_USER`. |
| `created_at` | TIMESTAMP | Yes | No | Account creation timestamp. |
| `updated_at` | TIMESTAMP | Yes | No | Last update timestamp. |
| `last_login_at` | TIMESTAMP | No | No | Timestamp of last successful login. |

## SQL Schema (PostgreSQL)

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    full_name VARCHAR(100) NOT NULL,
    provider VARCHAR(20) NOT NULL,
    provider_id VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_email ON users(email);
```
