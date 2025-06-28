# Trip Planner Backend

This is a simple Spring Boot backend for the Trip Planner website.

## How to Run

1. Make sure you have Java 17+ and Maven installed.
2. In the `backend` directory, run:
   ```
   mvn spring-boot:run
   ```
3. The backend will start at `http://localhost:8080`.

## Endpoints

- `/api/packages` - List available packages
- `/api/bookings` - Create a new booking

You can connect your frontend to these endpoints using fetch/AJAX.
