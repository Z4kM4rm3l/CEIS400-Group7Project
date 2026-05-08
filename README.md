
**# ✅ ToolVault – Automated Equipment Checkout & Optimized Warehouse Inventory (Skeleton)

**Status:** Skeleton architecture ready for incremental development  
**Date:** November 28, 2025

---

## 📌 Overview
ToolVault is a modular, **Spring Boot–based system** designed to address tool loss and warehouse inefficiencies for **GB Manufacturing**.

- **Primary Architecture:** N-Layered (Presentation → Service → Repository → Domain)
- **Complementary Architecture:** Event-Driven (Kafka)
- **Tech Stack:**
    - Java 17
    - Spring Boot 3.3.x
    - Maven
    - H2 (dev) → MySQL (prod)
    - Kafka
    - OpenAPI (springdoc)
    - Actuator

---

## 📂 Repository Structure
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
├── docker-compose.yml             # MySQL + Kafka for local infra (optional)
└── .github/workflows/ci.yml       # GitHub Actions CI
```

---

## 🚀 Quick Start

### **Prerequisites**
- Java 17
- Maven 3.9+
- IntelliJ IDEA (recommended)

### **Build**
```shell
mvn clean install
```

### **Run a Service (Example: Identity)**
```shell
mvn -pl identity-service spring-boot:run
```

---

## ✅ Testing Instructions (Module-Level)

### **1. Identity Service (RBAC, MFA skeleton)**
**Goal:** Verify endpoints and security config allow Swagger/Actuator access.  
Run:
```shell
mvn -pl identity-service test
```
**Tests to include:**
- `IdentityControllerSmokeTest`: Assert `/swagger-ui.html` and `/actuator/health` return **200 OK**.
- `SecurityConfigTest`: Ensure `permitAll()` for Swagger paths.

**Manual Check:**  
Visit [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) and confirm endpoints load.

---

### **2. Depot Ops Service (Checkout/Check-in skeleton)**
**Goal:** Validate controller stubs and domain mapping.  
Run:
```shell
mvn -pl depot-ops-service test
```
**Tests to include:**
- `DepotControllerSmokeTest`: Assert `/checkout` and `/checkin` return **501 Not Implemented**.
- `EntityMappingTest`: Validate JPA entity fields exist (e.g., Equipment, Badge).

**Manual Check:**
```shell
curl -X POST http://localhost:8082/checkout
```

---

### **3. Warehouse Ops Service (Inventory skeleton)**
**Goal:** Confirm inventory endpoints and entity persistence.  
Run:
```shell
mvn -pl warehouse-ops-service test
```
**Tests to include:**
- `WarehouseControllerSmokeTest`: `/inventory` and `/transfer` return **501**.
- `RepositoryIntegrationTest`: Use H2 to persist `InventoryItem` and assert retrieval.

---

### **4. Procurement Service**
**Goal:** Validate order endpoint stub.  
Run:
```shell
mvn -pl procurement-service test
```
**Tests to include:**
- `ProcurementControllerSmokeTest`: `/order` returns **501**.

---

### **5. Reporting Service**
**Goal:** Validate audit and usage endpoints.  
Run:
```shell
mvn -pl reporting-service test
```
**Tests to include:**
- `ReportingControllerSmokeTest`: `/audit` and `/usage` return **501**.

---

### **6. Notification Service**
**Goal:** Validate `/ping` endpoint and Kafka consumer stubs.  
Run:
```shell
mvn -pl notification-service test
```
**Tests to include:**
- `NotificationControllerSmokeTest`: `/ping` returns **200 OK**.

---

## ✅ Common Testing Setup
- JUnit 5 for unit tests
- Spring Boot Test for integration tests
- **Future:** Testcontainers for MySQL + Kafka event-driven tests

---

## 🔄 CI/CD Testing Integration
Update `.github/workflows/ci.yml` to run module-specific tests:
```yaml
- name: Run module tests
  run: mvn -pl identity-service,depot-ops-service,warehouse-ops-service,procurement-service,reporting-service,notification-service test
```

---

## 📈 Next Steps
- Make one line run command to boot all modules at once
- Polish end-point module integration
- Add Kafka event tests using Testcontainers
- Add security tests for JWT and MFA flows  
  **