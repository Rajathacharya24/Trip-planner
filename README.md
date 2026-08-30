Trip Planner

Project structure (organized)

- frontend/: Static website files (HTML, CSS, images)
  - index.html — Home page
  - explore.html — Destinations
  - booking.html — Booking flow and admin views
  - login.html / register.html — Auth pages
  - style.css — Styling for frontend

- backend/: Java Spring Boot application (server-side)
  - pom.xml, `src/main/java` and resources

Notes
- I moved all frontend files into `frontend/` to keep the repository root clean.
- Images remain at repository root and are referenced from `frontend/` as `../pexels-...jpg`.

If you want images moved into `frontend/`, I can copy them as well (binary files).