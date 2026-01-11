# Data Model: User Service

**Feature**: User Service (Registration, Auth, Profile)
**Schema Version**: 1.0

## Entity Relationship Diagram (Conceptual)

```mermaid
erDiagram
    USERS ||--|| USER_PROFILES: "has"
    USERS {
        UUID id PK
        String provider_name
        String provider_id
        Boolean enabled
        Timestamp created_at
        Timestamp updated_at
    }
    USER_PROFILES {
        UUID id PK
        UUID user_id FK
        String email UK
        String first_name
        String last_name
        String phone_number
        String address
        Timestamp created_at
        Timestamp updated_at
    }
```

## Schema Definitions (PostgreSQL)

### 1. Table: `users`

Core identity table. Handles authentication credentials and account status.

| Column          | Type         | Constraints  | Description                           |
|:----------------|:-------------|:-------------|:--------------------------------------|
| `id`            | UUID         | PK, Not Null | Unique identifier                     |
| `provider_name` | VARCHAR(50)  | Not Null     | Auth provider (LOCAL, GOOGLE, GITHUB) |
| `provider_id`   | VARCHAR(255) | Not Null     | ID from external provider             |
| `enabled`       | BOOLEAN      | Default TRUE | Account active status                 |
| `created_at`    | TIMESTAMP    | Not Null     | Audit timestamp                       |
| `updated_at`    | TIMESTAMP    | Not Null     | Audit timestamp                       |

**Indexes**:

- `idx_users_provider_identity` (Unique: provider_id, provider_name)

### 2. Table: `user_profiles`

Personal information separate from credentials. 1:1 relationship with `users`.

| Column         | Type         | Constraints        | Description   |
|:---------------|:-------------|:-------------------|:--------------|
| `id`           | UUID         | PK, Not Null       | Unique identifier |
| `user_id`      | UUID         | FK(`users.id`)     | Links to User |
| `email`        | VARCHAR(255) | UK, Not Null       | User login handle |
| `first_name`   | VARCHAR(100) | Nullable           |               |
| `last_name`    | VARCHAR(100) | Nullable           |               |
| `phone_number` | VARCHAR(20)  | Nullable           |               |
| `address`      | TEXT         | Nullable           |               |
| `created_at`   | TIMESTAMP    | Not Null           |               |
| `updated_at`   | TIMESTAMP    | Not Null           |               |

## Java Entities (JPA)

### `User.java`

```java

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "uq_users_provider", columnNames = {"provider_id", "provider_name"})
})
public class User extends AuditableEntity {
    private String providerName; // LOCAL, GOOGLE, GITHUB

    private String providerId;

    private boolean disabled = false;

    // BaseEntity handles created/updated_at with @PrePersist/@PreUpdate
}
```

### `UserProfile.java`

```java

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile extends AuditableEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String email;
    
    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phoneNumber;

    @Column
    private String address;
}
```

## Validation Rules

1. **Email**: Must be valid email format (Regex).
2. **Password**: Min 8 chars, mixed case/numbers (Enforced by Service layer before hashing).
3. **Phone**: E.164 format preferred.
