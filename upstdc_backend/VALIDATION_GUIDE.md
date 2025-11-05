# Validation Guide (Auth, Projects CRUD, File Upload)

Base URL (dev):
- Backend: http://localhost:3001 (via proxy) or http://localhost:8080 (direct)
- Swagger UI: http://localhost:3001/swagger-ui.html
- Health: http://localhost:3001/api/health and http://localhost:3001/actuator/health

1) Auth/Login
- POST /api/auth/login
  body: { "username": "admin", "password": "<your-admin-password>" }
  returns: { "accessToken", "refreshToken" }
- Use accessToken as Authorization: Bearer <token> for protected endpoints
- To refresh: POST /api/auth/refresh with { "refreshToken": "<token>" }

2) Projects CRUD
- GET /api/projects (public list for demo)
- GET /api/projects/{id}
- POST /api/projects (ADMIN): { "name": "Project A", "location": "City", "status": "Planned" }
- PUT /api/projects/{id} (ADMIN)
- DELETE /api/projects/{id} (ADMIN)

3) File Upload/Download
- POST /api/documents/project/{projectId}/upload (multipart/form-data, field "file")
  - Requires Authorization: Bearer <accessToken>
- GET /api/documents/{documentId}/download (requires Authorization)

Notes
- Default admin user is seeded by Flyway (V2__seed_admin.sql). If password unknown, generate a bcrypt via GET /api/dev/bcrypt?raw=yourpass (as ADMIN) and update the DB accordingly.
- Ensure CORS origin http://localhost:3000 is allowed for frontend calls.
