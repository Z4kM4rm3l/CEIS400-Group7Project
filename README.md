# ToolVault

> AI-powered inventory management system built for construction operations.

---

## Overview

ToolVault is a full-stack microservices application designed to streamline tool and equipment inventory management for construction companies. Built solo using an AI-first development approach with GitHub CoPilot, ToolVault demonstrates production-pattern architecture across seven independent services — containerized with Docker and backed by persistent MySQL.

**Key capabilities:**
- JWT authentication with role-based access control
- Real-time asset check-in / check-out tracking
- AI-powered forecasting that predicts shortages and suggests reorder quantities
- Low stock alerting and transfer request management
- Persistent refresh token system with one-time-use rotation
- Swagger/OpenAPI documentation across all services

---

## Architecture

```
React Frontend (Port 3000)
        │
        │ JWT Bearer Token
        ▼
┌─────────────────────────────────────────────┐
│              Spring Boot Services            │
├──────────────┬──────────────┬───────────────┤
│   Identity   │  Depot Ops   │  Warehouse    │
│   :8081      │  :8082       │  Ops :8083    │
│  Auth / JWT  │  Assets /    │  Stock /      │
│              │  Forecasting │  Inventory    │
├──────────────┼──────────────┼───────────────┤
│  Procurement │ Notification │  Reporting    │
│  :8084       │  :8086       │  :8085        │
└──────────────┴──────────────┴───────────────┘
        │
        │ HikariCP Connection Pool
        ▼
   MySQL 8.4 (Docker Volume — persistent)
```

**Tech stack:**
- Java 17 — Spring Boot 3.3.5
- Spring Security — JWT (HS256) + BCrypt
- Spring Data JPA — Hibernate 6
- MySQL 8.4 — H2 (local dev fallback)
- Docker + Docker Compose
- Maven multi-module build
- Swagger / OpenAPI 3 (Springdoc)
- React frontend (in progress)

---

## Services

| Service | Port | Responsibility |
|---|---|---|
| `identity-service` | 8081 | Auth, JWT issuance, refresh token persistence |
| `depot-ops-service` | 8082 | Asset tracking, checkout history, AI forecasting |
| `warehouse-ops-service` | 8083 | Stock levels, restock, allocation |
| `procurement-service` | 8084 | Purchase request management |
| `notification-service` | 8086 | Email, SMS, event alerting |
| `reporting-service` | 8085 | Analytics and summary reports |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker Desktop

### Run with Docker

```bash
# Clone the repository
git clone https://github.com/Z4kM4rm3l/CEIS400-Group7Project.git
cd CEIS400-Group7Project

# Build all services
mvn clean package -DskipTests

# Start the full stack
docker-compose up --build
```

MySQL databases are created automatically on first run via `scripts/init.sql`.

### Environment Variables

All secrets are injected via Docker Compose environment variables. For local development, services fall back to H2 in-memory databases — no configuration needed.

For production, set the following in your environment or `.env` file:

```
SPRING_DATASOURCE_URL=jdbc:mysql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=...
JWT_ISSUER=...
JWT_ACCESS_EXP_MIN=15
```

---

## API Highlights

### Authentication

```bash
# Register
POST /auth/register
{ "firstName": "...", "lastName": "...", "email": "...", "password": "..." }

# Login
POST /auth/login
{ "email": "...", "password": "..." }

# Refresh token (one-time use rotation)
POST /auth/refresh
{ "refreshToken": "uuid-here" }
```

### Forecasting

```bash
# Checkout frequency by tool model (last 30 days)
GET /forecasting/frequency

# Tools at risk of shortage
GET /forecasting/risk

# AI-generated reorder suggestions
GET /forecasting/reorder
```

### Inventory

```bash
# List all stock
GET /warehouse/stock

# Top 5 SKUs by quantity
GET /warehouse/top-skus

# Check in / check out assets
POST /depot/check-in
POST /depot/check-out
```

---

## Security

- Passwords hashed with BCrypt
- JWTs signed with HMAC-SHA256
- Refresh tokens stored in MySQL with expiry and one-time-use revocation
- Role-based route protection (`USER`, `ADMIN`)
- Custom 401/403 JSON error responses
- Stateless session management

---

## Development Approach

ToolVault was built using an **AI-first development methodology** — treating GitHub CoPilot as a primary development partner rather than an autocomplete tool. Architectural decisions, service boundaries, security patterns, and business logic were directed by the developer; CoPilot handled syntax generation and boilerplate.

This approach mirrors the emerging discipline of agentic software development — where human judgment guides AI execution to ship production-quality systems faster.

---

## Author

**Zakary Marmel**
- BAS Software Design — GPA 3.93, Dean's List
- AAS Information Technology & Networking — With Honors
- [LinkedIn](https://www.linkedin.com/in/zak-marmel/)
- [GitHub](https://github.com/Z4kM4rm3l)

---

*Built with Java, Spring Boot, Docker, and AI-first development practices.*
