# N3TK Website Project

## Overview

The "N3TK" Website is a robust software solution aimed at enhancing the online shopping experience for customers while providing efficient management tools for shop owners and administrators. Tailored to the needs of a clothing retail business, this project encompasses a range of features addressing the requirements of both customers and shop owners.

## Technology Stack

### Frontend
- **React:** Utilize React for building an interactive and responsive user interface.

### Backend
- **Spring Boot:** Employ Spring Boot for developing a robust and scalable backend application.
- **Spring Security:** Implement Spring Security for handling user authentication and authorization.
- **Hibernate:** Use Hibernate for efficient data handling and database interactions.
- **MySQL:** Utilize MySQL as the relational database for storing and retrieving data.
- **OAuth 2.0:** Implement OAuth 2.0 for secure and standardized user authentication.
- **Docker:** Containerize the application using Docker for seamless deployment and scalability.

### Development Workflow
- **GitFlow:** Follow the GitFlow branching model for organized and collaborative development.
- **Digital Ocean VPS:** Deploy the application on a Virtual Private Server provided by Digital Ocean for reliability and scalability.

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Install dependencies:
   ```bash
   # Frontend
   cd frontend
   npm install

   # Backend
   cd backend
   ./mvnw install
   ```

3. Configure the environment:
   - Set up database configurations, API keys, and other environment-specific settings.

4. Run the application:
   ```bash
   # Frontend
   cd frontend
   npm start

   # Backend
   cd backend
   ./mvnw spring-boot:run
   ```

5. Access the application:
   Open your browser and navigate to the specified URL.

## License

This project is licensed under the [MIT License](LICENSE.md).
