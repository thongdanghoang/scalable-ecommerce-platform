# Scalable E-Commerce Platform Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-01-06

## Active Technologies

- **Language**: Java 21 (LTS)
- **Frameworks**: Spring Boot 3.x, Spring Security (Auth Server, OAuth2 Client), Spring Web, Hibernate (JPA), Flyway, Lombok, MapStruct
- **Database**: PostgreSQL 16+
- **Infrastructure**: Docker, Docker Compose

## Project Structure

```text
user-service/
├── src/main/java/com/ecommerce/userservice/
├── src/test/java/com/ecommerce/userservice/
├── Dockerfile
└── pom.xml
```

## Commands

- **Run Service**: `./mvnw spring-boot:run`
- **Test**: `./mvnw test`
- **Verify**: `./mvnw verify`
- **DB Migration**: Flyway (auto-run on startup or via plugin)

## Code Style

- **Java**: Standard Java naming conventions (CamelCase).
- **Lombok**: Use `@Data`, `@Builder` to reduce boilerplate.
- **MapStruct**: Use interfaces for mappers.

## Recent Changes

- **001-user-auth-profile**: Added User Service with Spring Boot, Spring Security, and PostgreSQL.

<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
