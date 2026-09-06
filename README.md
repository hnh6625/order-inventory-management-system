# OIMS - Order Inventory Management System

## Overview

OIMS (Order Inventory Management System) is an internal Order Management System (OMS) for a fashion brand selling products through multiple e-commerce marketplaces such as Shopee and TikTok Shop.

The system centralizes order and inventory management across sales channels and focuses on preventing overselling when multiple marketplaces send orders concurrently.

The project is built with Spring Boot and applies Domain-Driven Design (DDD), Hexagonal Architecture, TDD/BDD, concurrency control, security, and DevOps practices.

### Main Domain

The product model follows:

**Style → Variant → SKU**

Each Style can contain multiple Variants based on size and color. Each Variant has its own SKU, which is used to track inventory.

### Key Technical Challenges

* Preventing inventory overselling under concurrent orders from multiple marketplaces.
* Handling webhook idempotency to prevent duplicate orders when marketplaces retry requests.
* Separating external marketplace payloads from internal domain models.
* Supporting different authentication mechanisms for internal users and external systems.
* Designing marketplace integrations so additional channels can be added without tightly coupling the core domain to a specific marketplace.

---

## Tech Stack

### Backend

* **Java 21**
* **Spring Boot 4.1.0**
* **Spring Web MVC** — Build REST APIs
* **Spring Data JPA / Hibernate** — Persistence layer
* **Spring Security** — Authentication and authorization
* **JWT** — Authentication for internal users
* **API Key** — Authentication for external systems and webhooks
* **Bean Validation** — Request validation
* **PostgreSQL 16** — Relational database
* **Flyway** — Database migration
* **Redis 7** — Idempotency cache

### Architecture & Design

* **Domain-Driven Design (DDD)**
* **Hexagonal Architecture**
* **Modular Monolith**
* **Bounded Contexts**
* **Repository Pattern**
* **Adapter Pattern**
* **Factory Pattern**
* **Domain Events**
* **Value Objects**
* **Optimistic Locking**

### Testing

* **JUnit 5**
* **AssertJ**
* **Mockito**
* **Spring Boot Test**
* **Testcontainers**
* **Cucumber / Gherkin**

### API & Documentation

* **REST API**
* **OpenAPI / Swagger**

### DevOps

* **Docker**
* **Docker Compose**
* **Nginx** — Reverse Proxy
* **GitHub Actions** — CI/CD
* **GitHub Container Registry (GHCR)** — Docker image registry

---

## Getting Started

This section explains how to set up and run OIMS locally.

### Prerequisites

Make sure the following tools are installed:

| Tool           | Version | Purpose                                     |
| -------------- | ------- | ------------------------------------------- |
| Java           | 21      | Run and develop the Spring Boot application |
| Git            | Latest  | Clone and manage the source code            |
| Docker Desktop | Latest  | Run application dependencies and containers |
| Postman        | Latest  | Test REST APIs                              |
| DBeaver        | Latest  | Inspect PostgreSQL data                     |

Verify the installed tools:

```bash
java -version
git --version
docker --version
```

Java should be version 21.

Docker Desktop should be running before starting the application or running tests that use Testcontainers.

### Clone the Repository

Clone the project from GitHub and navigate into the project directory:

```bash
git clone https://github.com/hnh6625/order-inventory-management-system.git
cd order-inventory-management-system
```

### Environment Configuration

OIMS uses environment variables for database configuration.

Create a `.env` file in the project root:

```env
DB_NAME=oims
DB_USERNAME=oims_user
DB_PASSWORD=oims_pass
```

The `.env` file should not be committed to Git.

### Run with Docker Compose

Start the application and its dependencies:

```bash
docker compose up --build
```

Docker Compose starts the following services:

* Spring Boot application
* PostgreSQL
* Redis
* Nginx

The default architecture is:

```text
Client
  ↓
Nginx :80
  ↓
Spring Boot :8080
  ↓
PostgreSQL :5432

Redis :6379
```

### Check the Application

After the containers are started, check the application health:

```text
GET /actuator/health
```

A healthy application should return a response indicating that the application is up.

### Swagger UI

OIMS provides interactive API documentation using Swagger UI.

Open:

```text
/swagger-ui/index.html
```

Swagger can be used to:

* View available REST APIs.
* Inspect request and response models.
* Test API endpoints.
* Test authenticated APIs with JWT or API Key authentication.

### Running Tests

Run the complete test suite with Maven.

On Linux/macOS:

```bash
./mvnw clean test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd clean test
```

The test suite includes unit tests, integration tests, security tests, and concurrency-related tests.

### Running the Application Without Docker

The application can also be started directly using Maven.

Make sure PostgreSQL and Redis are running and the required environment variables are configured.

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The Spring Boot application runs on port `8080`.

When using Docker Compose, access the application through Nginx on port `80`.

### Stop the Application

To stop the Docker Compose environment:

```bash
docker compose down
```

To stop the containers and remove their associated volumes:

```bash
docker compose down -v
```

> Use `docker compose down -v` carefully because removing volumes also removes the PostgreSQL data stored in the Docker volume.

### Quick Start

For a quick local setup:

```bash
git clone https://github.com/hnh6625/order-inventory-management-system.git

cd order-inventory-management-system

docker compose up --build
```

Then open:

```text
/swagger-ui/index.html
```

The project is now ready for API testing with Swagger or Postman.

---

## Architecture

OIMS is designed as a **Modular Monolith** using **Domain-Driven Design (DDD)** and **Hexagonal Architecture**.

The application is organized into independent business modules, with each module responsible for a specific business capability.

### Main Modules

| Module               | Responsibility                                      |
| -------------------- | --------------------------------------------------- |
| `catalog`            | Manage Styles, Variants and product information     |
| `inventory`          | Manage stock, restocking and stock reservation      |
| `channelintegration` | Receive marketplace webhooks and handle idempotency |
| `ordering`           | Manage orders and order lifecycle                   |
| `fulfillment`        | Manage shipments and delivery status                |
| `security`           | Authentication and authorization                    |
| `shared`             | Shared Value Objects and common components          |

### Module Structure

Each business module follows the separation of **Domain**, **Application**, and **Infrastructure** layers.

```text
module/
├── domain/
│   ├── model/
│   ├── repository/
│   └── service/
│
├── application/
│   └── service/
│
└── infrastructure/
    ├── persistence/
    └── web/
```

### Domain-Driven Design

The project applies the following DDD concepts:

* **Entity**
* **Value Object**
* **Aggregate Root**
* **Domain Service**
* **Repository**
* **Domain Event**
* **Bounded Context**

The main Bounded Contexts are:

* **Catalog**
* **Inventory**
* **Channel Integration**
* **Ordering**
* **Fulfillment**

### Hexagonal Architecture

The application separates business logic from external technologies.

A simplified request flow is:

```text
HTTP Request
     ↓
Controller
     ↓
Application Service
     ↓
Domain
     ↓
Repository Interface
     ↓
Repository Adapter
     ↓
JPA / Hibernate
     ↓
PostgreSQL
```

The **Domain layer contains the core business rules** and is kept independent from infrastructure concerns such as JPA, Redis, and HTTP.

Repositories and external integrations are accessed through interfaces, while infrastructure provides their concrete implementations.

---

## Features

### Catalog Management

* Manage Styles and Variants.
* Generate SKU based on Style Code, Size and Color.
* Validate duplicate Variants within a Style.
* Validate product data at both API and Domain layers.

### Inventory Management

* View current inventory by SKU.
* Restock inventory.
* Reserve stock when receiving orders.
* Release stock when required.
* Prevent negative inventory.
* Use Optimistic Locking to handle concurrent stock updates.

### Marketplace Integration

* Receive orders from Shopee through simulated webhooks.
* Receive orders from TikTok Shop through simulated webhooks.
* Convert marketplace-specific payloads into internal domain models.
* Handle webhook idempotency to prevent duplicate order processing.
* Use Redis as a short-lived idempotency cache.
* Persist processed webhook information in PostgreSQL.

### Order Management

* Receive orders from external marketplaces.
* Reserve inventory before creating an order.
* Prevent duplicate orders using marketplace order identifiers.
* Manage order lifecycle and fulfillment information.

### Fulfillment

* Support `MARKETPLACE_MANAGED` fulfillment.
* Support `SELF_ARRANGED` fulfillment.
* Record carrier and tracking information for self-arranged shipments.
* Confirm delivery status through the fulfillment flow.

---

## Bounded Contexts

OIMS is divided into five main Bounded Contexts.

| Bounded Context         | Responsibility                                        |
| ----------------------- | ----------------------------------------------------- |
| **Catalog**             | Manage Styles, Variants and product information       |
| **Inventory**           | Manage stock and stock reservation                    |
| **Channel Integration** | Handle marketplace webhooks and external integrations |
| **Ordering**            | Manage orders and order lifecycle                     |
| **Fulfillment**         | Manage shipments and delivery status                  |

Each context owns its own business responsibilities and communicates with other parts of the system through defined interfaces and application services.

---

## Security

OIMS uses different authentication mechanisms for internal users and external systems.

### Internal Authentication

Internal users authenticate using **JWT**.

Supported internal roles:

| Role               | Responsibility                                  |
| ------------------ | ----------------------------------------------- |
| `SYSTEM_ADMIN`     | Manage products, users and system configuration |
| `OPERATIONS_STAFF` | Process orders and fulfillment operations       |
| `WAREHOUSE_STAFF`  | Manage and view inventory                       |

JWT authentication is handled by a dedicated security filter, while authorization is enforced using Spring Security roles.

### External System Authentication

Marketplace webhooks use **API Key authentication**.

Each external system sends its API key through the request header:

```text
X-API-Key: <api-key>
```

API Key authentication is handled separately from JWT authentication through a dedicated filter.

### Authorization

Examples of authorization rules:

* `/webhooks/**` requires the `MARKETPLACE` role.
* `/api/inventory/**` requires `WAREHOUSE_STAFF` or `SYSTEM_ADMIN`.
* `/api/styles/**` requires `SYSTEM_ADMIN`.
* `/api/orders/**` requires `OPERATIONS_STAFF` or `SYSTEM_ADMIN`.
* `/api/fulfillment/**` requires `OPERATIONS_STAFF` or `SYSTEM_ADMIN`.

---

## Concurrency & Idempotency

### Concurrency Control

One of the main technical challenges of OIMS is preventing inventory overselling when multiple marketplaces send orders concurrently.

Example:

```text
Initial stock = 8

Shopee order   → reserve 8
TikTok order   → reserve 8

Both requests arrive concurrently.
```

Without concurrency control, both requests could read the same stock value and incorrectly reserve the inventory.

OIMS uses **Optimistic Locking** with JPA `@Version` to detect concurrent updates.

When two transactions attempt to update the same stock record, the version field is used to detect a conflicting update.

This prevents concurrent requests from incorrectly applying the same stock state.

### Idempotency

Marketplaces may retry a webhook when they do not receive a successful response.

Without idempotency:

```text
Shopee webhook
      ↓
Create Order
      ↓
Shopee retries
      ↓
Create Order again
```

This could create duplicate orders.

OIMS prevents this by using the marketplace order ID together with the channel as the idempotency key.

The system uses:

* PostgreSQL persistence for processed webhook records.
* Redis as a short-lived idempotency cache.
* Persistent database records as the source of truth.

Example Redis key:

```text
webhook:SHOPEE:{marketplaceOrderId}
```

Redis is used to speed up duplicate webhook detection, while PostgreSQL provides persistent tracking of processed webhooks.

---

## Database

OIMS uses **PostgreSQL 16** as the primary relational database.

### Main Database Areas

| Area                | Main Data           |
| ------------------- | ------------------- |
| Catalog             | Styles, Variants    |
| Inventory           | Stock Items         |
| Ordering            | Orders, Order Lines |
| Fulfillment         | Shipments           |
| Security            | Users and roles     |
| Channel Integration | Processed Webhooks  |

### Database Migration

Database schema changes are managed using **Flyway**.

Migrations are versioned and stored in:

```text
src/main/resources/db/migration/
```

This allows the database schema to be reproduced consistently across environments.

---

## API

OIMS exposes REST APIs through Spring Web MVC.

### Authentication

```text
/api/auth/**
```

Used for internal authentication.

### Catalog

```text
/api/styles/**
```

Used to manage Styles and Variants.

### Inventory

```text
GET  /api/inventory
GET  /api/inventory/{sku}
POST /api/inventory/restock
```

### Orders

```text
/api/orders/**
```

Used for order-related operations.

### Fulfillment

```text
POST /api/fulfillment/shipments
/api/fulfillment/**
```

Used to manage shipment and fulfillment operations.

### Marketplace Webhooks

```text
POST /webhooks/shopee/orders
POST /webhooks/tiktokshop/orders
```

These endpoints simulate order callbacks from external marketplaces.

### API Documentation

Interactive API documentation is available through **Swagger UI / OpenAPI**.

```text
/swagger-ui/index.html
```

---

## Testing

Testing is an important part of the OIMS development process.

The project uses:

* JUnit 5
* AssertJ
* Mockito
* Spring Boot Test
* Testcontainers
* Cucumber / Gherkin

### Unit Testing

Unit tests are used to verify domain and application business rules independently.

Examples include:

* Prevent duplicate Variants.
* Generate SKU correctly.
* Reject invalid prices.
* Reserve stock correctly.
* Reject insufficient stock.

### Integration Testing

Integration tests verify interactions between the application and infrastructure components.

Examples include:

* Repository persistence.
* PostgreSQL integration.
* Redis integration.
* Spring Security behavior.

### Concurrency Testing

OIMS includes tests that simulate multiple concurrent stock reservations.

Example scenario:

```text
Initial stock = 10
Concurrent requests = 20
Each request attempts to reserve 1 item

Expected:
Successful reservations + remaining stock = 10
```

Optimistic Locking is used to detect concurrent stock updates and prevent incorrect inventory state.

### BDD

Cucumber / Gherkin is used to describe important business scenarios in a behavior-oriented format.

Example:

```gherkin
Feature: Receive order from marketplace

Scenario: Reject order when there is insufficient stock
  Given the inventory does not contain enough stock
  When a marketplace sends an order
  Then the order should be rejected
```

---

## Docker & Deployment

OIMS is containerized using Docker.

### Docker Architecture

```text
                    Client
                      │
                      ▼
                ┌───────────┐
                │   Nginx   │
                │ Reverse   │
                │  Proxy    │
                └─────┬─────┘
                      │
                      ▼
                ┌───────────┐
                │ Spring    │
                │ Boot App  │
                └─────┬─────┘
                      │
             ┌────────┴────────┐
             ▼                 ▼
       ┌───────────┐     ┌───────────┐
       │ PostgreSQL│     │   Redis   │
       └───────────┘     └───────────┘
```

### Docker Compose

Docker Compose is used to run the main services together:

* Spring Boot application
* PostgreSQL
* Redis
* Nginx

Start the system with:

```bash
docker compose up --build
```

### Docker Image

The application uses a multi-stage Docker build:

1. Maven + JDK 21 for building the application.
2. Lightweight JRE image for running the application.

This separates the build environment from the runtime environment and keeps the runtime image lightweight.

### Nginx

Nginx acts as a reverse proxy in front of the Spring Boot application.

```text
Client
  ↓
Nginx :80
  ↓
Spring Boot :8080
```

---

## CI/CD

GitHub Actions is used to automate the CI/CD pipeline.

The workflow performs:

1. Checkout source code.
2. Setup JDK 21.
3. Run Maven tests.
4. Login to GitHub Container Registry.
5. Build the Docker image.
6. Push the Docker image to GHCR.

Docker images are published to:

```text
ghcr.io/hnh6625/order-inventory-management-system:latest
```

The CI pipeline ensures that changes are tested before the Docker image is published.

---

## Project Structure

```text
OIMS/
├── src/
│   ├── main/
│   │   ├── java/com/example/oims/
│   │   │   ├── catalog/
│   │   │   ├── inventory/
│   │   │   ├── channelintegration/
│   │   │   ├── ordering/
│   │   │   ├── fulfillment/
│   │   │   ├── security/
│   │   │   ├── shared/
│   │   │   └── config/
│   │   │
│   │   └── resources/
│   │       └── db/migration/
│   │
│   └── test/
│
├── docs/
│   ├── adr/
│   │   └── ADR-001-why-postgresql.md
│   └── requirements.md
│
├── nginx/
│   └── nginx.conf
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Documentation

Additional project documentation is available in the `docs/` directory.

### Requirements

```text
docs/requirements.md
```

Contains the business requirements and system scope of OIMS.

### Architecture Decision Records

```text
docs/adr/
```

Contains important architecture decisions and the reasoning behind them.

Examples of architecture decisions include:

* Why PostgreSQL?
* Why Modular Monolith?
* Why Optimistic Locking for inventory?
* Why Redis for idempotency?
* Why Adapter Pattern for marketplace integration?
* Why API Key for external webhook authentication?
* Why JWT for internal users?

---

## Future Improvements

The current OIMS implementation focuses on building a strong Java Backend Fresher portfolio project.

Potential future improvements include:

### Microservices

Split the current Modular Monolith into independent services when the system requires independent deployment and scaling.

Potential services:

```text
Catalog Service
Inventory Service
Ordering Service
Channel Integration Service
Fulfillment Service
```

### Event-Driven Architecture

Introduce asynchronous communication using messaging technologies such as Kafka or RabbitMQ.

Potential use cases include:

* Order events.
* Inventory events.
* Product synchronization.
* Fulfillment events.

### Cloud & DevOps

Future deployment improvements may include:

* Cloud infrastructure.
* Kubernetes.
* Advanced CI/CD pipelines.
* Infrastructure as Code.

### Performance & Scalability

Potential improvements include:

* Database query optimization.
* Redis caching.
* Connection pool tuning.
* Asynchronous processing.
* Load testing.
* Horizontal scaling.

### Real Marketplace Integration

The simulated Shopee and TikTok Shop integrations can later be replaced or extended with real marketplace APIs.

The Adapter Pattern allows external integrations to be changed without tightly coupling the core domain to a specific marketplace.

---

## Project Documentation

The project contains the following main documentation:

| Document               | Description                                        |
| ---------------------- | -------------------------------------------------- |
| `README.md`            | Project overview, architecture, features and setup |
| `docs/requirements.md` | Business requirements and system scope             |
| `docs/adr/`            | Architecture decisions                             |
| Swagger / OpenAPI      | REST API documentation                             |

---

## Learning Outcomes

By completing OIMS, the project demonstrates practical experience with:

* Java 21 and modern Java development.
* Spring Boot backend development.
* REST API design.
* Spring Data JPA and PostgreSQL.
* Domain-Driven Design.
* Hexagonal Architecture.
* Modular Monolith architecture.
* Repository and Adapter Patterns.
* Value Objects and Domain Models.
* Transaction management.
* Optimistic Locking.
* Concurrency control.
* Webhook integration.
* Idempotency.
* Redis caching.
* JWT authentication.
* API Key authentication.
* Role-based authorization.
* Unit and integration testing.
* TDD / BDD concepts.
* Docker and Docker Compose.
* Nginx reverse proxy.
* GitHub Actions CI/CD.
* Docker image publishing with GHCR.

The main engineering challenges demonstrated by the project are:

1. Preventing inventory overselling under concurrent requests.
2. Handling duplicate marketplace webhooks safely.
3. Separating external marketplace payloads from internal domain models.
4. Supporting different authentication mechanisms for users and external systems.
5. Designing marketplace integrations using interfaces and adapters.

---

## Interview Talking Points

The project can be discussed around the following technical problems.

### 1. Multi-channel Order Management

OIMS centralizes orders and inventory for a fashion brand selling through multiple marketplaces.

### 2. Overselling Prevention

Multiple marketplaces may send orders for the same SKU simultaneously.

Optimistic Locking is used to detect conflicting stock updates and protect inventory consistency.

### 3. Webhook Idempotency

Marketplaces may retry webhooks because of network failures or timeouts.

The system uses marketplace order identifiers, persistent processed-webhook records and Redis caching to avoid processing the same order multiple times.

### 4. Adapter Pattern

Marketplace-specific payloads and integrations are isolated behind interfaces and adapters.

This makes it possible to add another marketplace without tightly coupling the core business logic to a specific external platform.

### 5. Security

The project separates authentication mechanisms according to the type of client:

```text
Internal users
    ↓
JWT + Role

External systems
    ↓
API Key
```

This reflects the difference between user authentication and machine-to-machine authentication.

### 6. Architecture

The project uses a Modular Monolith instead of immediately splitting the system into microservices.

This keeps deployment and development relatively simple while maintaining clear business boundaries through modules and Bounded Contexts.

---

## Conclusion

OIMS is a backend-focused Order Management System designed to demonstrate practical Java Backend engineering skills through a realistic multi-channel e-commerce domain.

The project combines business-oriented domain modeling with production-oriented backend practices such as concurrency control, idempotency, security, testing, containerization and CI/CD.

The goal is not only to build a working application, but also to demonstrate the ability to reason about real backend engineering problems and make appropriate architectural decisions.
