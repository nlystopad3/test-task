# User Aggregator Service

User Aggregator Service is a Spring Boot application designed to aggregate user data from multiple databases. It provides a REST endpoint to fetch consolidated user information from all configured data sources.

## Features
- Aggregates user data from multiple databases.
- Supports configuration of infinite data sources.
- REST API with OpenAPI documentation.
- Supports both local and Dockerized deployment.

## Requirements
- **Java**: 17+
- **Maven**: 3.6+
- **Docker** (for containerized deployment)

---

## How to Run the Project Locally

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repository-directory>
```

### 2. Configure the Data Sources
Update the file `src/main/resources/application.yml` with the database connection details. Example:

```yaml
database-properties:
  datasources:
    -
      name: data-base-1
      strategy: postgres
      url: jdbc:postgresql://localhost:5433/db1
      table: users
      user: user1
      password: pass1
      mapping:
        id: user_id
        username: login
        name: first_name
        surname: last_name
    -
      name: data-base-2
      strategy: postgres
      url: jdbc:postgresql://localhost:5434/db2
      table: user_table
      user: user2
      password: pass2
      mapping:
        id: ldap_login
        username: ldap_login
        name: name
        surname: surname

```

### 3. Build the Application
```bash
./mvnw clean package
```

### 4. Start the Application
```bash
java -jar target/testTask-0.0.1-SNAPSHOT.jar
```

### 5. Access the API
- REST API Endpoint: [http://localhost:8080/users](http://localhost:8080/users)
- OpenAPI Documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## How to Run the Project with Docker

### 1. Build the Docker Image
Ensure the JAR file is built:
```bash
./mvnw clean package
```

Then build the Docker image:
```bash
docker build -t testtask .
```

### 2. Start the Application with Docker Compose
Run the following command to start the application and databases:
```bash
docker-compose up --build
```

### 3. Access the API
- REST API Endpoint: [http://localhost:8080/users](http://localhost:8080/users)
- OpenAPI Documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Docker Compose Configuration
The `docker-compose.yml` file includes the application and two PostgreSQL databases. Example:

```yaml
version: "3.8"
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
    depends_on:
      - postgres1
      - postgres2

  postgres1:
    image: postgres:15
    container_name: postgres1
    environment:
      POSTGRES_USER: user1
      POSTGRES_PASSWORD: pass1
      POSTGRES_DB: db1
    ports:
      - "5433:5432"

  postgres2:
    image: postgres:15
    container_name: postgres2
    environment:
      POSTGRES_USER: user2
      POSTGRES_PASSWORD: pass2
      POSTGRES_DB: db2
    ports:
      - "5434:5432"
```

---

## Notes
- Ensure Docker is running and the ports `8080`, `5433`, and `5434` are not used by other processes.
- Update database credentials and configuration as needed.
- Use the provided OpenAPI documentation to test and explore the API.



