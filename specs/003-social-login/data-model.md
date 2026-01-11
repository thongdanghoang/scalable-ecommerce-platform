# Data Model: Social Login Updates

**Feature**: Social Login (Google & GitHub)
**Schema Version**: 1.1 (Delta from 1.0)

## Schema Updates (PostgreSQL)

### 1. Table: `users` (Modified)

Changes to support social-only auth and composite keys.

| Column          | Change Type | New Definition        | Description                             |
|:----------------|:------------|:----------------------|:----------------------------------------|
| `password_hash` | **DROP**    | N/A                   | Password auth is disabled/removed.      |
| `provider_name` | **ADD**     | VARCHAR(50) NOT NULL  | IdP name (GOOGLE, GITHUB).              |
| `provider_id`   | **ADD**     | VARCHAR(255) NOT NULL | Unique ID from the IdP (Subject ID).    |

**Indexes**:

- **DROP** `idx_users_email` (Unique constraint removed).
- **ADD** `idx_users_provider_identity` (UNIQUE: provider_id, provider_name) - **Primary Logical Key**.

### 2. Table: `user_profiles` (Modified)

Changes to support social profile sync.

| Column       | Change Type | New Definition   | Description                                       |
|:-------------|:------------|:-----------------|:--------------------------------------------------|
| `avatar_url` | **ADD**     | TEXT NULL        | Synced from Google/GitHub 'picture'/'avatar_url'. |
| `locale`     | **ADD**     | VARCHAR(10) NULL | Synced from 'locale'.                             |

## Java Entities (JPA)

### `User.java` (Updated)

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uq_users_provider", columnNames = {"provider_id", "provider_name"}))
public class User extends AuditableEntity {
    // Inherits: 
    // - UUID id (PK) from BaseEntity
    // - int version (Optimistic Lock) from BaseEntity
    // - createdDate, lastModifiedDate from AuditableEntity

    @Column(nullable = false)
    private String providerName; // GOOGLE, GITHUB

    @Column(nullable = false)
    private String providerId; // External Subject ID (Logical Key Part)

    private boolean disabled = false;
}
```

### `UserProfile.java` (Updated)

```java

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile extends BaseEntity {
    // ... existing fields ...

    private String avatarUrl;
    private String locale;
}
```

## Attribute Mapping Strategy

### Name Mapping

Since `UserProfile` uses separate `firstName` and `lastName` fields, the system MUST map the provider's name attributes
as follows:

1. **Google (OIDC Standard)**:
    - Map `given_name` claim → `first_name`
    - Map `family_name` claim → `last_name`

2. **GitHub (Non-Standard)**:
    - GitHub primarily provides a single `name` field (e.g., "John Doe").
    - **Strategy**: Split the `name` string by the first space character.
        - `first_name`: Substring before the first space (or whole string if no space).
        - `last_name`: Substring after the first space (or `null`/empty if no space).
    - **Fallback**: If `name` is null/empty, use the GitHub `login` (username) as `first_name`.

## Validation Rules

1. **Provider+ID**: Must be unique combination.
2. **Email**: Valid format if present, but optional.
3. **Provider Enum**: Must be one of defined values (GOOGLE, GITHUB).
