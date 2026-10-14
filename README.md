# Logistics Shipment Tracking API

A RESTful logistics management API built with **Java 21, Spring Boot 4, PostgreSQL, and Docker**.

This project demonstrates backend engineering practices such as layered architecture, shipment lifecycle management, transaction handling, database migrations, and automated testing.

## Features

- Shipment CRUD operations
- Shipment status tracking and history
- Shipment lifecycle validation
- REST API input validation and structured error responses
- Database transactions and automatic rollback on failure
- Versioned database migrations using Flyway
- Unit testing with JUnit 5 and Mockito
- Integration testing with PostgreSQL
- Docker Compose for local deployment

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4 | Backend framework |
| Spring Data JPA / Hibernate | Database persistence |
| PostgreSQL 16 | Relational database |
| Flyway | Database migrations |
| JUnit 5 / Mockito | Automated testing |
| Maven | Build and dependency management |
| Docker Compose | Containerized deployment |

## Running with Docker

**Requirements:** Docker and Docker Compose.

1. Copy `.env.example` to `.env`.
2. Configure `DB_USERNAME` and `DB_PASSWORD` in `.env`.
3. Build the application:

   `./mvnw -DskipTests package`

4. Start the containers:

   `docker compose up --build -d`

The API will be available at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/shipments` | List shipments |
| GET | `/api/shipments/{id}` | Get a shipment |
| POST | `/api/shipments` | Create a shipment |
| PUT | `/api/shipments/{id}` | Update a shipment |
| DELETE | `/api/shipments/{id}` | Delete a shipment |
| GET | `/api/shipments/{id}/tracking` | Get tracking history |
| POST | `/api/shipments/{id}/tracking` | Add tracking event |

## Example: Create Shipment

```bash
curl -X POST http://localhost:8080/api/shipments \
  -H "Content-Type: application/json" \
  -d '{
    "trackingNumber": "SHIP-001",
    "origin": "Jakarta",
    "destination": "Surabaya"
  }'
```

## Shipment Lifecycle

The following status transitions are supported:

`CREATED → PICKED_UP → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED`

Shipments can also be cancelled before delivery. Once a shipment is `DELIVERED` or `CANCELLED`, no further status transitions are allowed.

## Automated Testing

Run all tests using:

```bash
./mvnw test
```

The test suite includes unit tests for shipment business rules and integration tests for JPA persistence, tracking history, and transaction rollback.

Integration tests use a separate PostgreSQL database named `logistics_test`. Configure and create this database before running the test suite.

## Database Migrations

Flyway automatically runs versioned SQL migrations during application startup.

Migration files are located at:

`src/main/resources/db/migration/`

Hibernate uses schema validation rather than automatically updating database tables.