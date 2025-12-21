# mediLabo-microservices

## Project Overview

MediLabo is a microservices-based application designed to help healthcare professionals
identify patients at risk of developing type 2 diabetes.

The application focuses on preventive healthcare by analyzing patient demographic data
and medical notes written by practitioners.  
Based on predefined medical rules, the system generates a diabetes risk assessment
for each patient.

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

## Technology Stack

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

Green Code

This project follows a Green Code approach by aiming to reduce unnecessary consumption of
CPU, memory, network bandwidth, storage, and energy, especially in a microservices-based
architecture.

The objective is not to fully optimize the system, but to understand environmental impacts,
identify improvement areas, and apply reasonable best practices.

---

### Green practices already applied

- **Use of DTOs** to limit the size of data exchanged between microservices and avoid sending unnecessary fields.
- **Pagination on list endpoints** (patients, notes) to reduce memory usage and network transfers.
- **Clear separation of responsibilities** between microservices to avoid duplicated processing.
- **Minimal and structured logging**, with reduced verbosity in production environments.
- **On-demand business processing**, such as calculating the diabetes risk only when requested.
- **Dockerized microservices**, allowing controlled resource usage and reproducible environments.
- **Simple and sober front-end design**, limiting unnecessary client-side processing and API calls.

---

### Potential green improvements

- Reduce unnecessary **inter-service calls** and optimize data retrieval flows.
- Improve **database indexing** and query optimization for frequently accessed fields.
- Further minimize **API payload sizes** and avoid over-fetching data.
- Apply **timeouts and retries tuning** to limit wasted CPU and network usage.
- Limit **CPU and memory allocation per container** in Docker Compose.
- Reduce **log volume and retention**, especially for non-critical information.
- Apply **trace sampling** in observability tools to avoid excessive telemetry data.
- Profile application performance to identify potential hotspots in CPU and memory usage.

## Présentation du projet

MediLabo est une application basée sur une architecture **microservices**, conçue pour aider
les professionnels de santé à identifier les patients présentant un risque de développer
un diabète de type 2.

L’application se concentre sur la **prévention médicale** en analysant les données
démographiques des patients ainsi que les notes médicales rédigées par les praticiens.  
À partir de règles médicales prédéfinies, le système génère une **évaluation du risque de diabète**
pour chaque patient.

Ce projet a été développé dans le cadre du **parcours Développeur Java OpenClassrooms – Projet 9**.

---

## Fonctionnalités principales

- Gestion des informations personnelles des patients
- Ajout, mise à jour et consultation des dossiers patients
- Ajout et consultation des notes médicales
- Génération de rapports d’évaluation du risque de diabète
- Quatre niveaux de risque possibles :
  - **None**
  - **Borderline**
  - **In Danger**
  - **Early Onset**

---

## Architecture

L’application repose sur une **architecture microservices**.

Chaque domaine métier est isolé dans son propre microservice et exposé via une
**API Gateway centralisée** développée avec **Spring Cloud Gateway**.

Tous les microservices sont **conteneurisés avec Docker** et orchestrés à l’aide de
**Docker Compose**.

---

## Microservices

- **patient-service** : gestion des données démographiques des patients
- **notes-service** : gestion des notes médicales (MongoDB)
- **assessment-service** : calcul du risque de diabète à partir des données patient et des notes
- **gateway-service** : point d’entrée centralisé et routage
- **front-service** : interface utilisateur web

---

## Stack technique

### Back-end

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Data JPA
- Spring Data MongoDB
- Spring Security

### Front-end

- Angular

### DevOps

- Docker
- Docker Compose

### Tests

- JUnit 5
- Testcontainers

---

## Sécurité

- APIs REST sécurisées avec **Spring Security**
- Authentification HTTP Basic
- Accès restreint aux données médicales des patients

---

## Lancer le projet

### Prérequis

- Docker
- Docker Compose

### Démarrer l’application

```bash
docker-compose up --build
```

## Green Code

Ce projet adopte une démarche Green Code, dont l’objectif est de réduire la consommation
inutile de ressources telles que :

- le CPU,
- la mémoire,
- la bande passante réseau,
- le stockage,
- et l’énergie.

Ces enjeux sont particulièrement importants dans une architecture microservices, où la
multiplication des services peut entraîner une surconsommation de ressources si elle n’est
pas maîtrisée.

L’objectif de ce projet n’est pas de rendre le système parfaitement optimisé d’un point de vue
environnemental, mais plutôt de :

- comprendre les impacts environnementaux du logiciel,
- identifier des axes d’amélioration possibles,
- appliquer des bonnes pratiques raisonnables et réalistes.

---

### Bonnes pratiques Green déjà mises en œuvre

- **Utilisation de DTOs** afin de limiter la taille des données échangées entre microservices et d’éviter l’envoi de champs inutiles.
- **Pagination des endpoints de type liste** (patients, notes) pour réduire la consommation mémoire et les transferts réseau.
- **Séparation claire des responsabilités** entre les microservices afin d’éviter les traitements redondants.
- **Journalisation minimale et structurée**, avec une verbosité réduite en environnement de production.
- **Traitements métier déclenchés à la demande**, comme le calcul du risque de diabète uniquement lorsqu’il est explicitement requis.
- **Microservices conteneurisés avec Docker**, permettant un meilleur contrôle de l’utilisation des ressources et des environnements reproductibles.
- **Interface front-end simple et sobre**, limitant les traitements inutiles côté client ainsi que les appels API superflus.

---

### Pistes d’amélioration Green potentielles

- Réduire les **appels inter-services inutiles** et optimiser les flux de récupération des données.
- Améliorer l’**indexation des bases de données** et optimiser les requêtes sur les champs les plus fréquemment utilisés.
- Réduire davantage la **taille des payloads des API** et éviter le surchargement des réponses (over-fetching).
- Ajuster finement les **timeouts et les stratégies de retry** afin de limiter les usages CPU et réseau inutiles.
- Limiter explicitement l’**allocation CPU et mémoire par conteneur** dans la configuration Docker Compose.
- Réduire le **volume de logs et leur durée de rétention**, en particulier pour les informations non critiques.
- Mettre en place un **échantillonnage des traces** dans les outils d’observabilité afin d’éviter une collecte excessive de données.
- Analyser les performances applicatives afin d’identifier les points de consommation excessive en CPU et en mémoire.
