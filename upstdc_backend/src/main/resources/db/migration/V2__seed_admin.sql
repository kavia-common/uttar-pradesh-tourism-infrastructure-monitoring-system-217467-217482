-- Development seed data (adjust/remove for production)
INSERT INTO roles (created_at, updated_at, name)
SELECT now(), now(), 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO users (created_at, updated_at, username, password_hash, full_name, enabled)
SELECT now(), now(), 'admin', '$2a$10$9p2y1k4bH7J0nVZ8mK2WnOr3HZc8O5i6v4j2Ao4M4ZrjzB8X3kT2e', 'Administrator', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username='admin' AND r.name='ADMIN'
ON CONFLICT DO NOTHING;
