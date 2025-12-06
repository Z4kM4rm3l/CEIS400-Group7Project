# Identity Service (JDK 17)


## Seeded Test Accounts (ready to use)
No manual data entry required. On startup, the service seeds:

- **Employee (USER role)**
  - Email: `employee@toolvault.local`
  - Password: `Emp123!`

- **Manager (ADMIN role)**
  - Email: `manager@toolvault.local`
  - Password: `Mgr123!`

Admin-only endpoint for verification:
- `GET /admin/ping` → requires `ADMIN` role
  - Manager: **200 OK** `{ "status": "pong" }`
  - Employee: **403 Forbidden**
