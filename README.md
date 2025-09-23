# Hospital API 🏥

## 📖 Description
Hospital API is a comprehensive hospital management system built with Java 17 and Spring Boot. It provides secure CRUD operations for hospital entities with authentication, validation, and follows clean architecture principles to ensure scalability and maintainability.

## 🏗️ Architecture
The project is structured in the following layers:
* **Main Package**: Contains the Spring Boot entry point (`HospitalApiApplication.java`)
* **Config**: Manages Spring Security, database configuration, and environment settings
* **Controller**: REST API endpoints with validation and security
* **Service**: Implements business logic and transaction management
* **Repository**: Interfaces for data access and Spring Data JPA repositories
* **Entity**: Domain models and JPA entities with Lombok annotations
* **Security**: Authentication and authorization configuration
* **DTO**: Data Transfer Objects for API communication

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
git clone https://github.com/your-username/hospital-api.git
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
    container_name: hospital-postgres
    environment:
      POSTGRES_DB: hospital_db
      POSTGRES_USER: hospital_user
      POSTGRES_PASSWORD: hospital_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

Then run:
```bash
docker-compose up -d
```

## ⚙️ Environment Configuration

1. You can find an example configuration file at `src/main/resources/.env.example`:
```properties
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/hospital_db
DB_USERNAME=hospital_user
DB_PASSWORD=hospital_password

# Server Configuration
SERVER_PORT=8080

# Security Configuration
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Application Configuration
APP_NAME=Hospital API
APP_VERSION=1.0.0
```

2. Create a `.env` file in the `src/main/resources/` directory and copy the variables from `.env.example`, replacing the values with your actual configuration.

3. Update `src/main/resources/application.properties` to reference these environment variables:
```properties
# Database Configuration
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=${SERVER_PORT}

# Security Configuration
hospital.app.jwtSecret=${JWT_SECRET}
hospital.app.jwtExpirationMs=${JWT_EXPIRATION}
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
│   │   │               ├── config/        # Security and database configuration
│   │   │               ├── controller/    # REST API controllers
│   │   │               ├── service/       # Business logic services
│   │   │               ├── repository/    # Spring Data JPA repositories
│   │   │               ├── entity/        # Domain models and JPA entities
│   │   │               ├── dto/           # Data Transfer Objects
│   │   │               ├── security/      # Security configuration and JWT
│   │   │               └── exception/     # Custom exceptions and handlers
│   │   └── resources/         
│   │       ├── application.properties     # Spring Boot configuration
│   │       └── .env.example              # Environment variables example
│   └── test/
│       └── java/                         # Unit and integration tests
├── docker-compose.yml                    # Docker services configuration
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

## 📚 API Documentation
Once the application is running, you can access:
* API endpoints at: `http://localhost:8080/api/`
* Health check at: `http://localhost:8080/actuator/health`

## 🤝 Contributing
To contribute to the project:
1. Fork the repository
2. Create a feature branch (`git checkout -b feat/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feat/new-feature`)
5. Create a Pull Request

## 📄 License
This project is licensed under the MIT License. See the LICENSE file for details.
