# UPSTDC Backend Quickstart

Prereqs
- Java 17+, Maven
- PostgreSQL running on localhost:5001 with DB "upstdc" and user/password "postgres"/"postgres"

Setup
1. Copy .env.example to your .env (or export variables in your shell).
2. Ensure DB is reachable at jdbc:postgresql://localhost:5001/upstdc.
3. Run: mvn spring-boot:run

On Startup
- Flyway will run migrations in src/main/resources/db/migration (V1, V2).
- Health endpoints:
  - http://localhost:8080/actuator/health
  - http://localhost:8080/api/health
- Swagger: http://localhost:8080/swagger-ui.html

Frontend
- Configure frontend API base to http://localhost:3001 for local integration (reverse proxy), or use http://localhost:8080 if calling backend directly (adjust CORS accordingly).
