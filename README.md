
# ToolVault – Automated Equipment Checkout & Optimized Warehouse Inventory (Skeleton)

**Status:** Skeleton architecture ready for incremental development  
**Date:** November 28, 2025

---

## Overview
ToolVault is a modular, Spring Boot–based system that addresses tool loss and warehouse inefficiencies for GB Manufacturing.

**Primary architecture:** N-Layered (Presentation → Service → Repository → Domain)  
**Complementary architecture:** Event-Driven (Kafka)  
**Tech:** Java 17, Spring Boot 3.3.x, Maven, H2 (dev) → MySQL (prod), Kafka, OpenAPI (springdoc), Actuator

---

## Repository Structure
```
toolvault_full/
├── pom.xml                        # Parent POM
├── common-lib/                    # Shared DTOs, Events, Enums, Crypto stub
├── identity-service/              # RBAC, MFA (skeleton)
├── depot-ops-service/             # Equipment check-in/out (skeleton)
├── warehouse-ops-service/         # Inventory, transfers, low-stock (skeleton)
├── procurement-service/           # Automated ordering (skeleton)
├── reporting-service/             # Audit/usage (skeleton)
├── notification-service/          # Alerts consumers (skeleton)
├── docker-compose.yml             # MySQL + Kafka for local infra (optional in skeleton)
└── .github/workflows/ci.yml       # GitHub Actions CI
```

Each service follows the N-layered package structure:
```
com.toolvault.<service_name>/
  ├── controller/       # REST endpoints (placeholder)
  ├── service/          # Business logic (TODO)
  ├── repository/       # Spring Data JPA interfaces
  ├── domain/           # JPA entities (basic fields)
  └── config/           # OpenAPI config
```

---

## Quick Start
### Prerequisites
- Java 17
- Maven 3.9+
- IntelliJ IDEA (recommended)

### Build
```bash
mvn clean install
```

### Run a Service (example: Identity)
```bash
mvn -pl identity-service spring-boot:run
```

### Swagger & Actuator
- Swagger UI: `http://localhost:<port>/swagger-ui.html`
- API Docs: `http://localhost:<port>/v3/api-docs`
- Health: `http://localhost:<port>/actuator/health`
- Info: `http://localhost:<port>/actuator/info`

Default ports:
```
identity-service:       8081
depot-ops-service:      8082
warehouse-ops-service:  8083
procurement-service:    8084
reporting-service:      8085
notification-service:   8086
```

---

## Development Profiles
- **Dev:** H2 in-memory (`application.yml` in each service) for instant startup.
- **Prod (later):** Switch to MySQL (see `docker-compose.yml`) and configure datasource.

---

## Event-Driven (Skeleton)
Stubs for producers/consumers exist; integrate Kafka later with topics such as:
- `equipment.checkout`
- `equipment.overdue`
- `inventory.lowstock`
- `procurement.order`

---

## Security (Skeleton)
Identity service includes a basic Spring Security config that permits Swagger/Actuator. RBAC, MFA (TOTP), 15-min session timeout, and JWT will be added during implementation.

---

## CI/CD
GitHub Actions workflow (`.github/workflows/ci.yml`) builds the project on pushes and PRs:
```yaml
name: Maven CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn -U -nsu clean install
      - name: Run tests
        run: mvn test
 
```

---

## Requirements Trace (at skeleton stage)
- **Subsystem A (Depot Ops):** Controllers stubs for `checkout`, `checkin` (TODO return). Domain entities created.
- **Subsystem B (Warehouse Ops):** Controllers stubs for `inventory`, `transfer`. Domain entities created.
- **Procurement:** `order` controller stub.
- **Reporting:** `audit`, `usage` stubs.
- **Notification:** `/ping` endpoint; consumer stubs planned.
- **Security:** Identity stubs (controllers, config).
- **Monitoring:** Actuator enabled in all services.
- **Docs:** springdoc-openapi enabled in all services.

---

## Next Implementation Steps
1. Implement Identity (RBAC roles, JWT, MFA).
2. Wire Kafka producers/consumers, define topics and event payloads.
3. Implement Depot Ops flows (badge-based checkout, condition logging, overdue alerts).
4. Implement Warehouse Ops (real-time visibility, transfers, low-stock alerts, reorder triggers).
5. Switch to MySQL for persistence; add migrations (Flyway/Liquibase).
6. Add unit & integration tests (JUnit, Testcontainers for MySQL/Kafka).
7. Harden security (TLS/HTTPS, VPN, AES-256 at rest via MySQL TDE or field-level).

---

## How to Submit via GitHub
1. Create a repo on GitHub (private or public).
2. From project root:
```bash
git init
git add .
git commit -m "ToolVault skeleton"
git branch -M main
git remote add origin https://github.com/<your-username>/<repo-name>.git
git push -u origin main
```
## Using GitHub for Versions (Architecture vs Completed Code)
Use **branches, tags, and releases**:
- **Branching:**
  - `main` → holds stable code.
  - `skeleton` → initial architecture-only version.
  - Feature branches → e.g., `feature/identity-mfa`, `feature/kafka-integrations`.
- **Tags:**
  - Tag the skeleton milestone: `v0.1.0-skeleton`.
- **Releases:**
  - Create a GitHub Release for the skeleton; later create `v1.0.0` for completed code.

**Workflow example:**
```bash
# Create skeleton branch and push
git checkout -b skeleton
git push -u origin skeleton

# Tag the skeleton state
git tag v0.1.0-skeleton
git push origin v0.1.0-skeleton

# Merge completed features into main later and tag release
git checkout main
# ... merge PRs ...
git tag v1.0.0
git push origin v1.0.0
```
This gives a clear view of the baseline architecture and the subsequent completed implementations.

---

## Contact
Questions or issues? Open an Issue on the repository.
