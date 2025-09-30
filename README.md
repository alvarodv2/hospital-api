# Hospital API 🏥

## 📖 Description
Hospital API is a comprehensive hospital management system built with Java 17 and Spring Boot. It provides secure CRUD operations for hospital entities with authentication, validation, and follows clean architecture principles to ensure scalability and maintainability.

## 🏗️ Architecture
The project follows a clean architecture pattern and is structured in the following layers:
* **Main Package**: Contains the Spring Boot entry point (`HospitalApiApplication.java`)
* **Controller**: REST API endpoints with validation and security
* **Service**: Business logic implementation and transaction management
* **Repository**: Spring Data JPA repositories for data access
* **Entity**: JPA entities (Doctor, Patient, Appointment, Prescription, Room, User)
* **DTO**: Data Transfer Objects for request/response handling
* **Security**: JWT authentication and authorization
* **Exception**: Custom exceptions and global exception handling

## 🚀 Technologies Used
* ☕ Java 17
* 🌱 Spring Boot
* 🔐 Spring Security
* ✅ Spring Boot Validation
* 📦 Maven
* 🐘 PostgreSQL (Database)
* 🐳 Docker (Database containerization)
* 🧪 JUnit 5 (Unit and integration tests)
* 🚀 GitHub Actions (CI/CD)

## 📋 Prerequisites
* ☕ JDK 17
* 📦 Maven 3.6.x or higher
* 🐳 Docker and Docker Compose
* 🐘 PostgreSQL (via Docker)

## 💾 Installation

1. Clone the repository:
```bash
git clone https://github.com/alvarodv2/hospital-api.git
```

2. Navigate to the project directory:
```bash
cd hospital-api
```

3. Build the project with Maven:
```bash
mvn clean install
```

## 🐳 Database Setup with Docker

1. Create and start the PostgreSQL container:
```bash
docker run --name hospital-postgres \
  -e POSTGRES_DB=hospital_db \
  -e POSTGRES_USER=hospital_user \
  -e POSTGRES_PASSWORD=hospital_password \
  -p 5432:5432 \
  -d postgres:15
```

Or use Docker Compose (create a `docker-compose.yml` file):
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:latest
    container_name: postgres-container
    env_file:
      - .env
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "${DB_PORT}:${DB_PORT}"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  hospital-api:
    build: .
    container_name: hospital-api
    env_file:
      - .env
    ports:
      - "${SERVER_PORT}:${SERVER_PORT}"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

Then run:
```bash
docker-compose up -d
```

## ⚙️ Environment Configuration

1. You can find an example configuration file:
```properties
# Database Configuration
DB_NAME=db_name
DB_USERNAME=db_username
DB_PASSWORD=db_password
DB_PORT=db_port

# TESTING
USER_NAME=user_name
PASSWORD=password

SERVER_PORT=server_port

JWT_SECRET=jwt_secret
JWT_EXPIRATION=jwt_expiration
```

2. Create a `.env` file and copy the variables from `.env.example`, replacing the values with your actual configuration.

3. Update `src/main/resources/application.properties` to reference these environment variables:
```properties
# Database Configuration
spring.config.import=optional:file:.env[.properties]
spring.datasource.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

server.port=${SERVER_PORT}

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
```

## ▶️ Execution

To run the application:
```bash
mvn spring-boot:run
```

Or execute the JAR file directly:
```bash
java -jar target/hospital-api-0.0.1-SNAPSHOT.jar
```

The API will be available at: `http://localhost:8080`

## 📁 Project Structure
```
├── .github/
│   └── workflows/
│       └── ci.yml                    # GitHub Actions CI/CD pipeline
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hospital/
│   │   │           └── api/
│   │   │               ├── HospitalApiApplication.java  # Main application class
│   │   │               ├── controller/    # REST API controllers
│   │   │               ├── service/       # Business logic services
│   │   │               ├── repository/    # Spring Data JPA repositories
│   │   │               ├── entity/        # Domain models and JPA entities
│   │   │               ├── dto/           # Data Transfer Objects
│   │   │               ├── security/      # Security configuration and JWT
│   │   │               └── exception/     # Custom exceptions and handlers
│   │   └── resources/         
│   │       ├── application.properties      # Spring Boot configuration
│   │       └── application-test.properties # Spring Boot Test configuration 
│   └── test/
│       └── java/                         # Unit and integration tests
├── docker-compose.yml                    # Docker services configuration
├── .env.example                          # Environment variables example
├── pom.xml                               # Maven dependencies and build config
└── README.md                             # Project documentation
```

## 🔐 API Security
This API uses Spring Security with JWT authentication. To access protected endpoints:

1. Register or login to get a JWT token
2. Include the token in the Authorization header: `Bearer <your-jwt-token>`

## 🧪 Testing
Run unit tests:
```bash
mvn test
```

Run integration tests:
```bash
mvn verify
```

## 🚀 CI/CD with GitHub Actions
This project includes GitHub Actions for continuous integration and deployment. The workflow automatically:
* Runs tests on pull requests
* Builds and validates the application
* Deploys to staging/production environments

Check `.github/workflows/` for configuration details.

## 📚 API Documentation (Swagger)
Once the application is running, you can access the API documentation through Swagger UI:

* **Swagger UI (Interactive Interface)**:
  ```
  http://localhost:8080/swagger-ui/index.html
  ```
* **OpenAPI Specification (JSON)**:
  ```
  http://localhost:8080/v3/api-docs
  ```
* **OpenAPI Specification (YAML)**:
  ```
  http://localhost:8080/v3/api-docs.yaml
  ```

### Using Swagger UI
1. Access the Swagger UI interface at `http://localhost:8080/swagger-ui/index.html`
2. To test protected endpoints:
   - First, use the `/api/auth/login` endpoint to get a JWT token
   - Click the "Authorize" button (🔓 icon)
   - In the authorization field, enter: `Bearer your-jwt-token`
   - Click "Authorize"
3. Now you can:
   - View all available endpoints
   - Test endpoints directly from the browser
   - See request/response models
   - View response codes and error descriptions

<img width="1490" height="358" alt="image" src="https://github.com/user-attachments/assets/1eedab6b-9f20-48ea-9a95-94a391cb23bb" />


## 🤝 Contributing
To contribute to the project:
1. Fork the repository
2. Create a feature branch (`git checkout -b feat/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feat/new-feature`)
5. Create a Pull Request

## 📄 License
This project is licensed under the MIT License. See the LICENSE file for details.
