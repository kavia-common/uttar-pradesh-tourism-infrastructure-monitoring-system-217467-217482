# Frontend Environment Configuration

Set the API base URL to the backend:
- Development: http://localhost:3001
- Production: your HTTPS domain

If using Angular default structure, ensure:
- src/environments/environment.ts:
  export const environment = {
    production: false,
    apiBaseUrl: (window as any)['NG_APP_API_BASE_URL'] || 'http://localhost:3001'
  };

- src/environments/environment.prod.ts:
  export const environment = {
    production: true,
    apiBaseUrl: (window as any)['NG_APP_API_BASE_URL'] || 'https://api.example.com'
  };

Alternatively, use a configuration service that reads from process.env or injected global window variables when building/deploying.

During local dev, ensure the frontend makes HTTP calls to environment.apiBaseUrl + '/api/...'
