# SSL/TLS Termination Strategy (Concise Notes)

Overview
- Terminate TLS at the reverse proxy/ingress (e.g., Nginx, Traefik, AWS ALB, or Kubernetes Ingress with cert-manager).
- Run the Spring Boot backend as HTTP internally (server.port=8080). The proxy forwards HTTPS -> HTTP.
- Frontend (Angular) is served over HTTPS and calls the backend via the proxy on HTTPS.

Recommended Topology
[Client HTTPS] -> [Reverse Proxy/Ingress:443 TLS certs] -> [Backend HTTP:8080]
                                                    \-> [Frontend HTTPS hosting]

Certificates
- Use Let's Encrypt (via cert-manager on K8s) or managed certificates from cloud provider.
- Keep private keys only at the proxy/ingress. Do not place certs inside application containers.

Headers
- Ensure proxy sets:
  - X-Forwarded-Proto=https
  - X-Forwarded-For=<client ip>
  - X-Forwarded-Host=<requested host>
- Optionally add Strict-Transport-Security (HSTS) at the proxy.

CORS
- Configure allowed origins to the frontend origin (e.g., https://app.example.com).
- In dev, http://localhost:3000 is allowed; in prod, set CORS_ALLOWED_ORIGINS to the production frontend URL.

Environment Variables (Prod)
- POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB, POSTGRES_PORT
- JWT_SECRET (>= 32 chars), JWT_EXPIRATION_MS, JWT_REFRESH_EXPIRATION_MS
- CORS_ALLOWED_ORIGINS
- FILE_STORAGE_BASE_PATH

Kubernetes Ingress Example (snippet)
- Use annotations for cert-manager:
  - cert-manager.io/cluster-issuer: letsencrypt
- Map host to backend service (port 8080) and to frontend.
- Attach TLS section with your domain and secretName.

Notes
- Enable Flyway migrations on startup (spring.flyway.enabled=true).
- Health endpoints exposed at /actuator/health and /api/health for liveness/readiness probes.
