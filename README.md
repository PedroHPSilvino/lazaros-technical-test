# User Management System

A full-stack User and Profile Management application built with **Spring Boot** and **Angular**.

This project was implemented as a technical assessment and focuses on **clean architecture, maintainability, validation, and user experience**.

It provides complete CRUD functionality for **Users** and **Profiles**, including relationship management, validation rules, and a modern UI.

---

# Tech Stack

## Backend
- Java 21
- Spring Boot 3
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit 5
- Mockito

## Frontend
- Angular 21
- Angular Material
- RxJS
- Standalone Components
- Angular Signals (Zoneless)

## Infrastructure
- Docker
- Docker Compose
- PostgreSQL 16

---

# Architecture Overview

The project follows a **layered architecture** in the backend and a **feature‑based structure** in the frontend.

## Backend Architecture

```
controller → service → repository → database
```

Layer responsibilities:

| Layer | Responsibility |
|------|------|
| Controller | HTTP API endpoints |
| Service | Business rules and validation |
| Repository | Database access |
| Entity | Persistence models |
| DTO | Request / response models |
| Mapper | Entity ↔ DTO conversion |

Benefits of this structure:

- Separation of concerns
- Maintainable business logic
- Easy unit testing
- Clean API boundaries

---

# Database Model

## Profile

Represents system roles.

| Field | Type |
|------|------|
| id | Long |
| description | String |

Validation:
- Required
- Minimum 5 characters

---

## User

Represents application users.

| Field | Type |
|------|------|
| id | Long |
| name | String |

Validation:
- Required
- Minimum 10 characters
- At least one profile assigned

Relationship:

```
User
  many-to-many
Profile
```

Users can have multiple profiles.

---

# Features

## Profile Management

- List profiles
- Create profiles
- Edit profiles
- Delete profiles
- Validation handling
- Error handling

---

## User Management

- List users
- Create users
- Edit users
- Delete users
- Assign multiple profiles
- Form validation

---

# UI Features

The frontend includes several UX improvements:

- Angular Material UI
- Reactive forms
- Profile multi-select
- Delete confirmation dialogs
- Snackbar success/error notifications
- Disabled submit buttons during requests
- Loading indicators for API calls
- Error handling

These features improve usability and provide a more production‑ready experience.

---

# Project Structure

```
backend/
  src/main/java/com/lazaros/usermanagement
    controller
    service
    repository
    entity
    dto
    mapper

frontend/
  src/app
    core
    features
      users
      profiles
    shared
```

---

# Running the Project

## Requirements

- Java 21
- Node 18+
- Docker

---

# Start Database

```
docker compose up -d postgres
```

PostgreSQL will run on:

```
localhost:5432
```

Database configuration:

```
database: usermanagement
user: postgres
password: postgres
```

---

# Run Backend

```
cd backend
./mvnw spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

---

# Run Frontend

```
cd frontend
npm install
npm start
```

Frontend runs at:

```
http://localhost:4200
```

---

# API Endpoints

## Profiles

```
GET    /api/profiles
GET    /api/profiles/{id}
POST   /api/profiles
PUT    /api/profiles/{id}
DELETE /api/profiles/{id}
```

---

## Users

```
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

---

# Example API Response

```
GET /api/users
```

```json
[
  {
    "id": 1,
    "name": "Pedro Henrique",
    "profiles": [
      {
        "id": 1,
        "description": "Administrator"
      },
      {
        "id": 2,
        "description": "Manager"
      }
    ]
  }
]
```

---

# Testing

Backend tests use:

- JUnit 5
- Mockito

Run tests:

```
./mvnw test
```

---

# Design Decisions

### Layered Backend Architecture

Separating controllers, services, and repositories keeps business logic isolated and improves maintainability.

### DTO Pattern

DTOs prevent exposing JPA entities directly and allow better API contracts.

### Angular Standalone Components

The frontend uses Angular’s standalone architecture to reduce module complexity and improve modularity.

### Angular Signals

Signals provide predictable reactive state updates and remove dependency on Zone.js.

### Reusable UI Components

Shared components were implemented for:

- Confirmation dialogs
- Notifications
- Loading indicators

This promotes consistency across the application.

---

# Possible Future Improvements

- Pagination for list endpoints
- Search and filtering
- Role-based authorization
- Integration tests for API
- End‑to‑end tests (Cypress / Playwright)
- CI/CD pipeline
- Containerized full‑stack deployment

---

# Author

Pedro Henrique