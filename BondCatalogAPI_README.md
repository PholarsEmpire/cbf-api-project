# 📘 Bond Catalog API

A Spring Boot REST API project for managing **fixed income securities/assets (bonds)**.  
This project simulates how an **investment bank** or **portfolio managers** might store, search, and analyze bonds.

---

## 🚀 Features
- CRUD operations for bonds
- Search & filtering by issuer, rating, coupon rate, face value, maturity date and so on.
- Bond lifecycle management (based on issue and maturity dates): Active, Matured, Defaulted
- Summary endpoint with statistics (total, avg coupon, highest/lowest coupon, number of unique issuers, etc.)
- Exception handling with clean, readable and easy to understand error responses
- Swagger UI documentation for each endpoint while also giving examples

---

## 🏗️ Tech Stack
- Java 21
- Spring Boot 3 (Web, JPA, DevTools)
- MySQL (database)
- Hibernate (ORM)
- Springdoc OpenAPI (Swagger UI)
- Maven (build tool)

---

## 👤 Intended Users
- **Reviewers/Mentors**: evaluating project design processes/principles
- **Finance/Data Professionals**: demoing bond management in the real-world
- **Junior Developers**: learning how to build Spring Boot APIs

---

## 📂 Project Structure
```
bondcatalog/
 ├── config/              → Sample data to test API and Swagger Configuration
 ├── controller/          → REST endpoints
 ├── entity/              → JPA entities
 ├── repository/          → Database queries
 ├── service/             → Business logic implementations
 ├── customexceptions/    → Possible exceptions handling
 ├── resources/           → Properties setup info
 └── README.md
```

---

## ⚙️ Setup Instructions

### Clone and Run
```bash
git clone https://github.com/PholarsEmpire/cbf-api-project.git
cd cbf-api-project
./mvnw spring-boot:run
```

### Database
```sql
CREATE DATABASE bond_catalog;
```

Configure `application.properties` with MySQL username/password.

### Swagger UI
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📡 Example Endpoints

**Create a Bond**
```http
POST /api/bonds
```

**Get Bonds by Status**
```http
GET /api/bonds/status?status=Active
```

**Summary**
```http
GET /api/bonds/summary
```

---

## ⚖️ Exception Handling
Errors are returned in JSON like format with timestamp, date and clear message:
```json
{
  "timestamp": "2025-08-22T12:34:56",
  "status": 404,
  "error": "Not Found",
  "message": "Bond with id 99 not found."
}
```

---

# 🧑‍💻 Developer Documentation

## 🎯 Purpose
To demonstrate how to design and implement a **production-style REST API** for fixed income securities using Spring Boot.

## 🔑 Design Choices
- **Layered architecture**: Controller → Service → Repository → DB
- **Exception handling**: centralized via `@RestControllerAdvice`
- **Validation**: input checks for coupon rates, face values, date ranges
- **REST best practices**: 
  - 200 OK with `[]` for empty searches
  - 404 for missing resources
  - 400 for invalid inputs

## 🏛️ Architecture Diagram
```
[Client/Swagger] → [Controller Layer] → [Service Layer] → [Repository] → [Database]
```

## 🧩 Entity Model (Bond)
- id (Long)
- name (String)
- issuer (String)
- faceValue (BigDecimal)
- couponRate (BigDecimal)
- rating (String)
- issueDate (LocalDate)
- maturityDate (LocalDate)
- status (derived: Active, Matured, Defaulted)

## ⚙️ Tools/Dependencies
- Spring Boot Starter Web (REST APIs)
- Spring Boot Starter Data JPA (ORM/Hibernate)
- MySQL Driver
- Springdoc OpenAPI UI (Swagger)
- Lombok (optional, for boilerplate reduction)

## ✅ Testing Strategy
- Unit tests for Service layer (which is where all business logics are implemented)
- Integration tests for Controller layer (API endpoints)
- H2 (in-memory DB) for tests

## 📈 Future Enhancements
- Add pagination & sorting
- JWT authentication & authorization
- Integrate with frontend dashboard
- Extend bond attributes (market, callable/putable flags)

---

## 📜 License
This API project is for educational purposes only, and NOT for investment advice.
