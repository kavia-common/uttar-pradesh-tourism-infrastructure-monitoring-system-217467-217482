# Cross-Container Integration Notes

Database (upstdc_database):
- Running PostgreSQL on localhost port 5001
- Backend dev config points to: jdbc:postgresql://localhost:5001/upstdc
- Override via env: POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB, POSTGRES_PORT

Backend (upstdc_backend):
- Spring Boot on 8080 internally; expected to be proxied to 3001 for frontend access.
- Flyway migrations enabled on startup: spring.flyway.enabled=true, locations=classpath:db/migration
- Health endpoints:
  - /actuator/health (Spring Actuator)
  - /api/health (custom)
- CORS allowed origin for dev: http://localhost:3000

Frontend (upstdc_frontend):
- Angular app expects API base URL: http://localhost:3001
- Set via environment file or NG_APP_API_BASE_URL env at runtime/build.

Security:
- JWT secret and expirations are env-configurable. Use a 32+ character secret in all environments.
- Allow specific production origin through CORS via CORS_ALLOWED_ORIGINS.
