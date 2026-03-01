# 🏭 Production Line System

Full-stack application built with:

-   Backend: Java 21 + Spring Boot\
-   Database: H2 (In-Memory)\
-   Frontend: Vue 3 + Vite\
-   Containerization: Docker + Docker Compose

------------------------------------------------------------------------

# 📦 Requirements

## To run manually (Development Mode)

### Backend

-   Java 21
-   Maven (or use mvnw wrapper)

### Frontend

-   Node.js 20+
-   npm

## Optional (Docker Mode)

-   Docker
-   Docker Compose (included in Docker Desktop)

------------------------------------------------------------------------

# 🚀 Running Manually (Development Mode)

## 1️⃣ Start Backend

Navigate to:

production_line/production-backend

Run:

mvn spring-boot:run

Or using wrapper:

./mvnw spring-boot:run

Backend will start on:

http://localhost:8080

### 🗄 Database

This project uses H2 In-Memory database.

Important: - Data exists only while the application is running - When
the backend stops, all data is lost - Restarting backend resets the
database

------------------------------------------------------------------------

## 2️⃣ Start Frontend

Navigate to:

production_line/production-frontend

Install dependencies:

npm install

If necessary:

npm install axios vue-router

Start frontend:

npm run dev

Frontend runs on:

http://localhost:5173

------------------------------------------------------------------------

# ✅ Manual Mode Summary

  Service    Port
  ---------- ------
  Backend    8080
  Frontend   5173

Make sure the backend is running before accessing the frontend.

------------------------------------------------------------------------

# 🐳 Running With Docker

If Docker is installed, you can run everything with one command.

------------------------------------------------------------------------

# 🚀 Run Everything With Docker

From root folder:

docker compose up --build

After build completes:

Frontend: http://localhost:5173

Backend: http://localhost:8080

------------------------------------------------------------------------

# 🛑 Stop Containers

docker compose down

------------------------------------------------------------------------

# 🧠 Important Notes

-   H2 database remains in-memory even inside Docker.
-   Stopping the backend container resets all data.
-   For persistence, configure H2 file mode or use PostgreSQL.

------------------------------------------------------------------------

# 🏁 Final Summary

You can run the project in two ways:

### Development Mode

-   Start backend manually
-   Start frontend manually

### Docker Mode

-   One command
-   Everything containerized
