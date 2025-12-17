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
