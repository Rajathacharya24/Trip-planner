# ✈️ TripPlanner - Travel Booking Application

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/Rajathacharya24/Trip-planner)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.6-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**TripPlanner** is a full-stack travel booking and destination exploration web application. It combines a responsive static frontend with a RESTful Spring Boot backend powered by JWT authentication, Flyway database migrations, and clean hexagonal/layered architecture.

---

## 🚀 Architecture & Tech Stack

### 🖥️ Frontend
- **Languages**: HTML5, CSS3, JavaScript (ES6+)
- **Styling & UI**: Bootstrap 5.3.3, Custom Theme CSS (`style.css`)
- **Features**: Responsive layouts, interactive booking wizard, dynamic navbar authentication state, Geolocation API integration, modal package preview.

### ⚙️ Backend
- **Framework**: Java 17+ / Spring Boot 3.2.6
- **Database**: H2 (In-memory development DB) with Flyway migrations
- **Security**: Spring Security 6 with JWT Stateless Authentication & Role-Based Access Control (`ROLE_USER`, `ROLE_ADMIN`)
- **Persistence**: Spring Data JPA with domain model adapters
- **Build Tool**: Apache Maven

---

## 📁 Repository Structure

```text
Trip-planner/
├── README.md                  # Project documentation
├── index.html                 # Homepage, featured packages & location tracker
├── booking.html               # Interactive multi-step booking page & admin dashboard
├── explore.html               # Destination showcase & detail view
├── login.html                 # User authentication login form
├── register.html              # New user registration form
├── style.css                  # Global custom stylesheet & theme variables
├── images/                    # Destination & showcase images
└── backend/                   # Spring Boot backend application
    ├── pom.xml                # Maven dependencies & build configuration
    └── src/
        ├── main/
        │   ├── java/com/tripplanner/backend/
        │   │   ├── application/       # Services & DTOs
        │   │   ├── domain/            # Domain models, repositories & exceptions
        │   │   └── infrastructure/    # Web controllers, JPA entities, Security & Config
        │   └── resources/
        │       ├── application.properties
        │       └── db/migration/      # Flyway SQL migrations (V1..V5)
        └── test/              # Comprehensive JUnit 5 & MockMvc unit test suite
```

---

## 🏁 Quick Start

### 1. Prerequisites
- **JDK 17** or higher
- **Apache Maven 3.8+** (or wrapper)
- A modern web browser

---

### 2. Backend Setup & Run

Navigate to the `backend` directory, build, and run the Spring Boot server:

```bash
cd backend

# Run unit test suite
mvn clean test

# Build and start the Spring Boot application
mvn spring-boot:run
```

- The API server starts on **`http://localhost:8082`**.
- H2 Console (if enabled in development mode): `http://localhost:8082/h2-console`

---

### 3. Frontend Setup & Run

The frontend consists of static web assets located in the root directory.

**Option A: Simple HTTP Server (Recommended)**
```bash
python3 -m http.server 8000
# Open http://localhost:8000 in your web browser
```

**Option B: Direct Browser Opening**
Open `index.html` directly in your browser.

---

## 🔑 REST API Documentation

Base Endpoint: `http://localhost:8082/api`

### 🔓 Public Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user account |
| `POST` | `/api/auth/login` | Authenticate user and receive JWT token |
| `GET` | `/api/packages` | Fetch list of available travel packages |
| `GET` | `/api/packages/{id}` | Get package details by ID |

### 🔒 User Endpoints (Requires `Authorization: Bearer <JWT>`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/bookings` | Create a new travel booking |
| `GET` | `/api/bookings/my` | Retrieve authenticated user's bookings |
| `GET` | `/api/bookings/{id}` | Get specific booking details (Owner/Admin) |
| `PUT` | `/api/bookings/{id}` | Update existing booking |
| `DELETE` | `/api/bookings/{id}` | Cancel booking |

### 🛡️ Admin Endpoints (Requires `ROLE_ADMIN`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/packages` | Add a new travel package |
| `PUT` | `/api/packages/{id}` | Edit travel package details |
| `DELETE` | `/api/packages/{id}` | Delete travel package |
| `GET` | `/api/bookings` | View all customer bookings (paginated) |
| `PUT` | `/api/bookings/{id}/status` | Update booking status (`PENDING`, `CONFIRMED`, `CANCELLED`) |

---

## 🛡️ Security Highlights

- **JWT Authentication**: Secure stateless token handling with configurable secret keys and expiration.
- **Password Hashing**: BCrypt encryption for user credentials.
- **Security Headers**: Production-grade response headers including Content-Security-Policy (CSP), X-Frame-Options (`DENY`), X-Content-Type-Options (`nosniff`), and Referrer-Policy.
- **CORS Support**: Configured origins for local web development.

---

## 🧪 Running Tests

To run the full backend test suite:

```bash
cd backend
mvn clean test
```

Tests include domain service logic validation, input parameter checks, and full Spring Security MockMvc controller layer testing.

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for details.
