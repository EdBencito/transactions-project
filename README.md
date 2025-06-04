# Event-Driven Microservices for Transaction Processing

A microservices system simulating a financial transaction platform using Apache Kafka, Avro, Docker, and
Spring Boot. This architecture showcases modern patterns such as asynchronous messaging, schema-based communication, and
service decoupling.
---

# Getting Started

## Prerequisites

*   Java 17
*   Docker + Docker Compose
*   PostgreSQL running locally (DB setup described below)

## Step 1: Start Kafka + Infrastructure + PostgreSQL

```bash
docker-compose up -d
```

This brings up:

* Kafka + Zookeeper

*  Confluent Schema Registry

* Kafdrop (Kafka web UI) at http://localhost:9000

```bash
docker run --name postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=transactions_project_db \
  -p 5432:5432 \
  -d postgres:16
```

This starts a fresh Postgres container with:

* User: postgres
* Password: postgres
* Database: transactions_project_db
* Port: 5432 on localhost

You can customize these values in the -e environment variables.

### Apply Schema SQL

Once the container is running, apply your SQL schema(s). From your host:
```bash
docker exec -i postgres psql -U postgres -d transactions_project_db < db/schema/account_service_schema.sql
docker exec -i postgres psql -U postgres -d transactions_project_db < db/schema/transaction_service_schema.sql
```

Or, using psql directly (if installed locally):

```bash
psql -h localhost -U postgres -d transactions_project_db -f db/schema/account_service_schema.sql
psql -h localhost -U postgres -d transactions_project_db -f db/schema/transaction_service_schema.sql
```
## Step 2: Run Services

In separate terminal tabs, navigate to each account-service & transaction-service and run:

```bash
DATASOURCE_URL=jdbc:postgresql://localhost:{PORT}/{YOUR_DB} \
DATASOURCE_USERNAME={YOUR_USERNAME} \
DATASOURCE_PASSWORD={YOUR_PASSWORD} \
./gradlew bootRun
```

&nbsp; Example:

```bash
cd account-service
DATASOURCE_URL=jdbc:postgresql://localhost:{PORT}/{YOUR_DB} \
DATASOURCE_USERNAME={YOUR_USERNAME} \
DATASOURCE_PASSWORD={YOUR_PASSWORD} \
./gradlew bootRun
```
and run these for transaction-processor-service & fraud-detection-service and run:

```bash
./gradlew bootRun
```

&nbsp; Example:

```bash
cd account-service
./gradlew bootRun
```

## Step 3: Access Swagger Docs (account-service & transaction-service)

Each service exposes its API docs via Swagger:

```bash
http://localhost:{PORT}/swagger-ui/index.html
```

(Adjust {PORT} for each service)

---

## Microservices Overview

| Service                   | Responsibility                                                   |
|---------------------------|------------------------------------------------------------------|
| **Account Service**       | Owns and manages account data and balance updates.               |
| **Transaction Service**   | Handles transaction creation, orchestrates Kafka events.         |
| **Transaction Processor** | Applies business rules to transactions (e.g., approval/decline). |
| **Fraud Detection**       | Flags transactions suspected to be fraudulent.                   |

All services communicate via **Kafka topics**, using **Avro-encoded messages** for structured, version-safe payloads.

---

## Event Flow

1. **Transaction Created**
   `TransactionService` commits to DB and produces `transaction.initiated`.

2. **Parallel Consumption**
   `TransactionProcessorService` and `FraudDetectionService` consume the event concurrently.

3. **Processing Results**

    * `TransactionProcessorService` produces `transaction.processed`
    * `FraudDetectionService` produces `transaction.flagged`

4. **Status Updates**
   `TransactionService` consumes both events and updates the transaction status accordingly.

5. **Balance Update**
   `TransactionService` produces `balance.update` for valid transactions.

6. **Final Commit**
   `AccountService` consumes the event and updates the account balance.

<a href="./images/transactions-project-sqeuence.svg" target="_blank">
  <img src="./images/transactions-project-sqeuence.svg" alt="Transaction Flow" width="600" />
</a>

---

## Technical Stack

* **Language**: Java 17
* **Framework**: Spring Boot (multi-module via Gradle)
* **Messaging**: Apache Kafka with Avro serialization
* **Database**: PostgreSQL (separate schema per for Account Service & Transaction Service)
* **Documentation**: Springdoc OpenAPI (Swagger UI)
* **Communication**: Kafka & Feign clients (for internal REST)

---

## Kafka Topics

* `transaction.initiated`
* `transaction.processed`
* `transaction.flagged`
* `balance.update`

---

## Avro & Schema Registry

* Each Kafka message uses a dedicated **Avro schema**.
* Managed centrally with **Confluent Schema Registry**.
* Ensures **backward compatibility** and schema evolution.

---

## Dockerized Infrastructure

| Component           | Description                                 |
|---------------------|---------------------------------------------|
| **Kafka**           | Core message broker                         |
| **Zookeeper**       | Required by Kafka for coordination          |
| **Schema Registry** | Manages Avro schemas                        |
| **Kafdrop**         | UI for inspecting Kafka topics and messages |
| **PostgreSQL**      | Persistent storage per service schema       |

All of the above are configured in Docker Compose for local development.

---

## Swagger Documentation

All APIs are documented using **Springdoc OpenAPI 3**.

Access each service’s Swagger UI at:

```
http://localhost:{PORT}/swagger-ui/index.html
```

---

## Project Structure

```
root-project/
├── account-service/
├── transaction-service/
├── transaction-processor-service/
├── fraud-detection-service/
├── common-avro-schemas/
├── shared-library/
├── docker-compose.yml
└── README.md
```

Each service is a self-contained Spring Boot module built with Gradle, wired via Kafka.

---

## Notes

* The system supports **parallel, independent message processing**.
* Custom repository methods are used to cross-reference transaction and account IDs.
* Feign clients facilitate inter-service communication without tight coupling.
* The project is structured as a **multi-module Gradle build**.
* Kafka infrastructure includes **Zookeeper** and **Schema Registry**.

---

## Areas for Improvement

This project is a work in progress with several improvements under consideration:

* **Replace Feign Clients:**
    * Evaluate replacing Feign with asynchronous messaging or gRPC to reduce service coupling and improve resiliency.

* **Introduce Comprehensive Testing**
    * Future versions will include unit tests, integration tests, and contract testing using tools like Testcontainers
      and Pact.

* **Replace Internal Controllers with K6 Load Testing**
    * Eliminate ad hoc controller endpoints and rely on K6 scripts for performance and stress testing.

* **General Refactoring**
    * Clean up redundant or unused code, improve DTO-layer consistency, and streamline internal service
      responsibilities.

* **Include PostgreSQL in Docker Compose**
    * potentially package PostgreSQL with the kafka related containers; option to automate schema setup using mounted
      scripts or init containers.

* **Retry & DLQ Handling for Kafka**
    * Add retry strategies and dead-letter queues for robust event consumption, especially under transient failure.

* **Monitoring and Observability**
    * Add Prometheus and Grafana for metrics, along with distributed tracing (e.g., OpenTelemetry or Zipkin).

* **Centralized Logging**
    * Consider ELK stack (Elasticsearch, Logstash, Kibana) or Grafana Loki for unified log storage and search.

---

> This project serves as a foundation for learning distributed system design, event-driven architecture, and real-world
> backend engineering practices. Improvements, and refactoring ideas are welcome.
