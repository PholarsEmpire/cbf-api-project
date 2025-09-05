# 📘 Fixed Income (Bond) Catalog API Developed by Folashade Olaitan

A Spring Boot REST API project for managing and analyzing **bonds** — fixed-income financial instruments, enriched with **real-time exchange rate/currency conversion** and **macro economic data** (such as GDP and Inflation) via external APIs. This project is designed as part of a software engineering bootcamp with enterprise-grade best practices in **Spring Boot**, **JPA**, **MySQL/H2**, **Swagger/OpenAPI**, **exception handling**, and **testing (JUnit5, Mockito, Testcontainers, JaCoCo)**.

---

## 📘 What is a Bond?

A **bond** is like a loan that investors give to governments, corporations, or institutions.

- When you buy a bond, you’re lending your money to the issuer.  
- The issuer promises to **pay you interest (coupon)** at agreed intervals.  
- At the end of the bond’s life (**maturity date**), the issuer returns your original investment (**face value**).  

👉 Think of it this way: **you act like the bank, and the issuer is the borrower.**

Bonds are popular because they offer:
- steady returns
- lower risk  compared to stocks,
- a predictable maturity date

---

## 🎯 Purpose of This API

The **Fixed Income (Bond) Catalog API** was built to:  

- 📂 Store, manage, and retrieve information about bonds. **Full CRUD operations.**
- 🔎 Provide filtering & search endpoints (by issuer, rating, coupon rate, maturity date, face value, or status).  
- 📊 Offer a **summary endpoint** with bond statistics (total bonds, average coupon, upcoming maturities etc).  
- 🌍 **External API Integrations**
  - 💱 Exchange rates (via [ExchangeRate API](https://exchangerate.host/))
  - 🌍 World Bank economic indicators (GDP, inflation)
- 🛡️ Showcase backend engineering best practices.  

💡 It is designed as both a **developer-friendly demo project** and a foundation for real-world financial apps. In short: this API mimics a simplified **bond catalog system** like one you might find in an **investment bank or financial platform**, but tailored for **learning and demonstration purposes**.

---

## 🚀 Features

- **CRUD Operations** for Bonds (`create`, `read`, `update`, `delete`).
- **Search & Filtering**:
  - By issuer, rating, coupon rate, maturity/issue dates, and status.
- **Catalog Summary**:
  - Total bonds
  - Average coupon rate
  - Highest/lowest coupon bonds
  - Next maturity bond
  - Number of maturities in the next 90 days
  - Count of unique issuers
- **Status Tracking**: Active, Matured, Defaulted.
- **Error Handling**: Global exception handler with meaningful error messages.
- **OpenAPI/Swagger Docs**.
- **Inheritance Demonstration**: `Asset` → `Bond` (future extensibility to other asset classes like REITs).
- **Unit & Integration Tests** with >95% coverage enforced by JaCoCo.

---

## 🏗️ Project Structure

```text
bondcatalog/
├── src/
│   ├── main/
│   │   ├── java/com/folaolaitan/bondcatalog/
│   │   │   ├── controller/          # REST controllers (BondController, etc.)
│   │   │   ├── entity/              # JPA entities (Bond, Asset, etc.)
│   │   │   ├── repository/          # Spring Data JPA repositories (BondRepository)
│   │   │   ├── service/             # Service layer (BondService)
│   │   │   ├── customexceptions/    # Custom exceptions & global handler
│   │   │   └── config/              # Swagger and WebClient Configurations & DataInitializer
│   │   └── resources/
│   │       ├── application.properties (or application.yml) # DB configs, profiles
│   │       └── static / templates   # (Optional) for static assets / views
│   └── test/
│       └── java/com/folaolaitan/bondcatalog/
│           ├── controller/          # Controller tests (MockMvc)
│           ├── service/             # Unit tests with Mockito
│           └── integration/         # Integration tests with H2 DB
├── .github/workflows/maven.yml      # GitHub Actions CI/CD workflow
├── pom.xml                          # Maven dependencies & plugins (WebTools, Data JPA,JaCoCo, etc.)
├── mvnw / mvnw.cmd                  # Maven wrapper scripts
├── sampleData.sql                   # A list of 30 sample data in mySQL DB to test my endpoints.
└── README.md                        # Project documentation
```

---

## 🛠️ Tech Stack

- **Java 17**  
- **Spring Boot 3** (Web, Data JPA)  
- **MySQL** (main DB for development) + **H2** (test DB)  
- **Swagger / OpenAPI 3** (API documentation)  
- **JUnit 5 + Mockito** (unit for service and controller classes)  
- **Spring Boot Test + H2** (integration tests)  
- **JaCoCo** (test coverage reporting with a minimum of 95% test coverage enforced)  
- **GitHub Actions** (CI/CD pipelien to build, test and generate coverage reports)

---

## ⚙️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/PholarsEmpire/cbf-api-project
cd cbf-api-project
```

### 2. Configure Database
- Create a **MySQL** database:
```sql
CREATE DATABASE bondcatalog;
```
- Update your `application.properties` or `application.yml`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bondcatalog
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

### 3. Run the application
```bash
./mvnw spring-boot:run
```
App starts at: `http://localhost:8080`  

Swagger UI available at: `http://localhost:8080/swagger-ui.html`  

H2 Console: `http://localhost:8080/h2-console`

---


## 🚀 Endpoints

### 📘 Bond Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/bonds` | List all bonds |
| GET    | `/api/bonds/{id}` | Get bond by ID |
| POST   | `/api/bonds` | Create new bond |
| PUT    | `/api/bonds/{id}` | Update existing bond |
| DELETE | `/api/bonds/{id}` | Delete bond |
| GET    | `/api/bonds/issuer/{issuer}` | Search bonds by issuer |
| GET    | `/api/bonds/rating/{rating}` | Filter by rating |
| GET    | `/api/bonds/coupon-rate/{rate}` | Bonds with coupon rate ≥ a specified value |
| GET    | `/api/bonds/coupon-rate/{min}/{max}` | Gets all Bonds within a specified coupon range |
| GET    | `/api/bonds/maturing-between/{start}/{end}` | Bonds maturing between the specified dates |
| GET    | `/api/bonds/maturity-date/{date}` | Bonds maturing after the specified date |
| GET    | `/api/bonds/issued-between?start-date=YYYY-MM-DD&end-date=YYYY-MM-DD` | Bonds issued between dates |
| GET    | `/api/bonds/issue-date/{date}` | Bonds issued after date |
| GET    | `/api/bonds/face-value/{value}` | Bonds with face value ≥ specified value |
| GET    | `/api/bonds/face-value-between?min-value=X&max-value=Y` | Bonds by face value range |
| GET    | `/api/bonds/status?status=Active` | Bonds by status (Active, Matured, Defaulted) |
| GET    | `/api/bonds/summary` | Bond catalog summary |

---

### 🌍 External API Endpoints

#### 💱 Exchange Rates
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/external/fx?from=USD&to=NGN` | Get exchange rate from one currency to another |
| GET    | `/api/external/macro/bonds/{id}/value-in?currency=XXX` | Convert bond’s face value into another currency |

#### 📊 World Bank
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/external/macro/{country}/gdp?year=2022` | GDP for a country (USD current) |
| GET    | `/api/external/macro/{country}/inflation?year=2022` | Inflation (CPI %) for a country |


### 🔹 Summary
- `GET /api/bonds/summary` → Returns:  
```json
{
  "totalBonds": 30,
  "avgCouponRate": 4.85,
  "uniqueIssuers": 18,
  "highestCoupon": 12.5,
  "highestCouponBondName": "Dangote Cement 2029",
  "lowestCoupon": 0.8,
  "lowestCouponBondName": "Japanese Government Bond 15Y",
  "nextMaturityBondName": "Eurobond - BMW",
  "nextMaturityBondDate": "2028-04-15",
  "maturitiesInNext90Days": 2
}
```
---

## 📖 API Docs

Swagger UI is available at:
👉 http://localhost:8080/swagger-ui.html

---

## ⚡ Configuration

### `application.properties`

```properties
# External API config
exchange.api.url=https://api.exchangerate.host
exchange.api.key=0462701b613db5f7c008664dd19d0ac4
exchange.api.timeout=5000

---

## 🧪 Testing

### Unit Tests
- **BondService** tested with JUnit 5 and Mockito  
- Validates business rules & exception handling  

### Controller Tests
- Uses **MockMvc** to test REST endpoints  
- Ensures correct HTTP responses & error codes  

### Integration Tests
- Uses **Spring Boot Test + H2 DB**  
- Verifies repository queries & DB layer  

### Code Coverage: Enforced at **95% minimum with Jacoco**

Run all tests:
```bash
./mvnw test
```

Generate Jacoco Report:
```bash
./mvnw clean verify
```

### Code Test Coverage (Used JaCoCo)
- Enforced a **95% minimum coverage**  
- HTML report generated at: `target/site/jacoco/index.html`

---

## 🔄 CI/CD (GitHub Actions)

Runs on every push & PR

Steps:

- Build project with Maven

- Run all tests

- Generate JaCoCo report

- Upload test reports as artifacts

👉 See workflow: .github/workflows/maven.yml

---
## Seed Data
👉 Sample Data (to test endpoints) located at ./sampleData.sql



---
## 🚧 Future Improvements/Development

### 1) API Design & UX
- Add **pagination, sorting, and filtering** on list endpoints (`GET /api/bonds?size=50&page=0&sort=maturityDate,asc`).
- Introduce **API versioning** (e.g., `/api/v1/...`) and **standard error responses** (RFC 7807 Problem Details).
- Support **partial updates** with `PATCH` (e.g., update only `rating`).
- Enable **bulk operations** (bulk create/update/delete).

### 2) Data Model & Domain Depth
- Add entities for **Issuer**, **RatingHistory**, **CouponSchedule**.
- Extend inheritance to include **REITs, Treasury Bills, Municipal Bonds, Eurobonds**.
- Add bond analytics: **yield-to-maturity**, **accrued interest**, **duration**, **convexity**.
- Support **multi-currency** with exchange rates.
- Implement **soft deletes** and **audit fields** (`createdBy`, `updatedAt`) via Spring Data auditing.

### 3) Reliability, Performance & Observability
- Add **database indexes** on searchable fields (issuer, rating, maturityDate).
- Implement **caching** (Spring Cache + Caffeine/Redis) for frequent queries like `/summary`.
- Apply **rate limiting** and request size limits.
- Add **metrics & tracing**: Micrometer + Prometheus/Grafana, OpenTelemetry tracing.
- Expose **health checks** and **readiness probes** (`/actuator/health`, `/actuator/info`).

### 4) Security & Governance
- Integrate **Spring Security** with JWT/OAuth2 authentication.
- Enforce **input validation** (Bean Validation) and strict CORS rules.
- Manage secrets using **environment variables** or **Vault**.
- Support **idempotency keys** for safe POST/PUT requests.
- Add **audit logging** and track user actions.

### 5) Documentation & Developer Experience
- Expand **Swagger/OpenAPI docs** with examples and detailed schemas.
- Generate **client SDKs** (TypeScript/Java) from OpenAPI spec.
- Use **DTO mapping** (e.g., MapStruct) to decouple entities from API payloads.
- Provide **API Guidelines** for naming, status codes, and error formats.
- Include a **Postman/Insomnia collection** in the repo.

### 6) Testing Strategy
- Maintain high coverage; add **mutation testing** (PIT).
- Implement **contract tests** (Spring Cloud Contract).
- Replace H2 with **Testcontainers (MySQL)** for realistic integration tests.
- Add **performance tests** (Gatling/JMeter).
- Write **property-based tests** for date and number edge cases.

### 7) Data Lifecycle & Migrations
- Use **Flyway/Liquibase** for schema migrations.
- Add **import tooling** for CSV/Excel bond data with validations.
- Define an **archiving strategy** for historical bonds.

### 8) Deployability & Environments
- **Dockerize** with multi-stage builds.
- Use **Spring profiles** for `dev`, `test`, `prod`.
- Provide **Helm/Kubernetes manifests** or **Docker Compose** for deployments.
- Explore **blue/green** or **canary** deployments.

### 9) Integrations & Events
- Integrate with external **rating feeds** (mock S&P/Moody’s) or bond price APIs.
- Use **Kafka/RabbitMQ** for async events (e.g., bond status changes).
- Add **scheduled jobs** for recalculating bond statuses.
- Provide **webhooks** for downstream consumers (alerts, Slack, email).

### 10) Product Features for Finance Users
- Add **portfolio endpoints** (positions, cashflows, P&L).
- Support **watchlists** and **alerts** (maturity, rating downgrade).
- Provide **search suggestions/autocomplete** for issuers.
- Add **export endpoints** (CSV/Excel).

### 11) Frontend & Visualization
- Build a **React dashboard** with charts (rating distribution, maturity ladder).
- Support **interactive filters** and saved searches.
- Add authentication with **OAuth2/OIDC** for frontend users.
---


## 👩‍💻 Author/Maintainer

Project developed as part of a software engineering bootcamp by: **Fola Olaitan**  
💡 Built with Java, Spring Boot & ❤️ for financial data.
Contact at: **fola@folaolaitan.com** and **+44 7404 545 876**

---
