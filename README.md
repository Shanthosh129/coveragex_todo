#  Full Stack To-Do Application

Welcome to my submission for the Full Stack Engineer Assessment! 

This is a containerized web application built to manage daily tasks efficiently. It features a modern, responsive UI with Dark Mode support, a robust Spring Boot backend, and a MySQL database—all orchestrated via Docker.

---



## Quick Start (The Only Step You Need)

You do **not** need Java, Node.js, or MySQL installed on your machine. You only need **Docker**.

1. **Clone the repository** (if you haven't already).
2. Open your terminal in the project root folder.
3. Run the application:

```bash
docker-compose up --build
```

---

### Once the containers are running, access the components here:

| Component | URL | Notes |
|---|---|---|
| Frontend UI | http://localhost | The React Application (Served via Nginx) |
| Backend API | http://localhost:8080/api/v1/tasks | The Spring Boot REST API |
| Database | localhost:3307 | Access via Workbench |

### Architecture & Tech Stack

The application follows a Clean Architecture principle with a strict separation of concerns:

1. **Frontend (Client)**
   - Tech: React (TypeScript), Vite, Tailwind CSS.
   - Highlights:
     - Uses React Query for state management and caching.
     - Framer Motion for smooth animations.
     - Fully responsive "Split-Screen" layout.
     - Dark/Light Mode support (persisted in LocalStorage).
     - Production Build: Served via Nginx (Alpine Linux) for high performance.
2. **Backend (Server)**
   - Tech: Java 21, Spring Boot 3.x.
   - Highlights:
     - Layered Architecture: Controller -> Service -> Repository.
     - REST API: Follows standard HTTP methods (GET, POST, PUT).
     - Security: CORS configured to allow communication with the Nginx frontend.
     - Validation: Strict input validation using Jakarta Validation (@NotBlank).
3. **Database (Persistence)**
   - Tech: MySQL 8.0.
   - Highlights:
     - Data persistence using Docker Volumes (data survives container restarts).
     - Hibernate/JPA automatically manages the schema (ddl-auto: update).

### Feature Checklist

This submission covers all requirements listed in the assessment:

- **Create Tasks:** Users can add a title and description.
- **Recent Tasks Only:** The UI strictly displays the top 5 most recent tasks.
- **Mark as Done:** Completed tasks are removed from the view.
-  **Update Task:** Users can modify existing tasks.
- **Dockerized:** The entire stack runs with `docker-compose`.
- **REST API:** Backend exposes a RESTful API.
- **Unit & Integration Tests:** Backend tests using JUnit and MockMvc.
- **Clean Code:** TypeScript interfaces match Java DTOs; logic is separated from UI.

Extra Marks:

- **Pretty UI:** Glassmorphism design, empty states, and animations.
- **Dark Mode:** Fully supported.
