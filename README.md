# mediLabo-microservices

## Project Overview

MediLabo is a microservices-based application designed to help healthcare professionals
identify patients at risk of developing type 2 diabetes.

The application focuses on preventive healthcare by analyzing patient demographic data
and medical notes written by practitioners.  
Based on predefined medical rules, the system generates a diabetes risk assessment
for each patient.

This project was developed as part of the **OpenClassrooms Java Developer Path – Project 9**.

---

## Functional Features

- Manage patient personal information
- Add, update and view patient records
- Add and view medical notes for patients
- Generate diabetes risk assessment reports
- Four possible risk levels:
  - **None**
  - **Borderline**
  - **In Danger**
  - **Early Onset**

---

## Architecture

The application follows a **microservices architecture**.

Each business domain is isolated in its own microservice and exposed through a centralized
API Gateway built with **Spring Cloud Gateway**.

All microservices are **containerized using Docker** and orchestrated with **Docker Compose**.

---

## Microservices

patient-service : Manages patient demographic data
notes-service : Manages medical notes using MongoDB
assessment-service : Calculates diabetes risk based on patient data and notes
gateway-service : Centralized entry point and routing
front-service : Web user interface

---

## 🛠 Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Data JPA
- Spring Data MongoDB
- Spring Security

### Frontend

- Angular

### DevOps

- Docker
- Docker Compose

### Testing

- JUnit 5
- Testcontainers

---

## Security

- Secured REST APIs using **Spring Security**
- HTTP Basic authentication
- Restricted access to patient medical data

---

## How to Run the Project

### Prerequisites

- Docker
- Docker Compose

### Run the application

```bash
docker-compose up --build
```

## Green Code Considerations

This project integrates Green Code principles by focusing on reducing unnecessary
resource consumption while maintaining performance and reliability.

Identified Green Code Improvements

1. Reduce unnecessary inter-service calls

Avoid redundant REST calls between microservices.

Cache frequently accessed data (e.g. patient information) to limit network usage.

2. Optimize memory usage

Use lightweight DTOs instead of full entities.

Avoid loading unnecessary data from databases.

Prefer simple loops over heavy stream operations for large collections.

3. Database optimization

Use MongoDB indexes on frequently queried fields (e.g. patientId).

Optimize SQL schemas by using appropriate column types and indexes.

4. Frontend optimization

Limit HTTP requests by updating UI state locally when possible.

Reduce unnecessary page reloads and component re-rendering.

5. Docker and infrastructure efficiency

Use lightweight Docker images (JRE / Alpine based).

Limit CPU and memory usage for containers.

Avoid running unused services in production.

6. Logging optimization

Reduce log verbosity in production environments.

Avoid excessive debug logs to limit I/O and disk usage.
