# Quickstart: User Service

## Prerequisites

- Java 21+
- Docker & Docker Compose
- Maven 3.9+

## Running the Service (Proposed)

1. **Start Infrastructure**:
   ```bash
   docker-compose up -d postgres
   ```

2. **Run Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Verify Health**:
   ```bash
   curl http://localhost:8080/actuator/health
   ```

## Testing

- **Unit Tests**:
  ```bash
  ./mvnw test
  ```
- **Integration Tests**:
  ```bash
  ./mvnw verify
  ```
