# Lumière Beauty API (Spring Boot backend)

REST API for the Lumière Beauty React shop (`../src/services/api.js` calls this
backend on `http://localhost:8080/api`).

## Requirements

- JDK 21 (or 22/23). Run `java -version` to check.
- Nothing else — Maven and the database need no installation:
  - the **Maven wrapper** (`./mvnw`) downloads Maven itself,
  - the database is **H2 in memory**: it is created fresh at every start and
    pre-seeded with the demo catalogue and demo accounts.

## Run the API

```bash
cd backend
./mvnw spring-boot:run        # or: ./mvnw -DskipTests package && java -jar target/lumiere-beauty-api.jar
```

Then:

- API: http://localhost:8080/api
- H2 database console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:lumiere`, user `sa`, empty password)

Demo accounts (seeded on first start):

| Role | Email | Password |
|---|---|---|
| ADMIN | `admin@lumiere.com` | `Admin123!` |
| CUSTOMER | `customer@lumiere.com` | `Customer123!` |

## Endpoints

| Method | Path | Access | Purpose |
|---|---|---|---|
| GET | `/api/products` | public | List products. Filters: `brand`, `category`, `search`, `status`, `featured` |
| GET | `/api/products/{id}` | public | One product (e.g. `P-1001`) |
| POST | `/api/products` | ADMIN | Create product (id auto-generated, next `P-xxxx`) |
| PUT | `/api/products/{id}` | ADMIN | Update product |
| DELETE | `/api/products/{id}` | ADMIN | Delete product (hard delete) |
| GET | `/api/brands` | public | Distinct brand names |
| POST | `/api/auth/register` | public | Create CUSTOMER account → `{token, tokenType, expiresInMs, user}` |
| POST | `/api/auth/login` | public | Verify credentials → same response shape |
| GET | `/api/auth/me` | any user | Current user from the Bearer token |
| POST | `/api/orders` | any user | Place order: reduces stock, snapshots prices |
| GET | `/api/orders/my` | any user | Signed-in user's orders (newest first) |
| GET | `/api/admin/orders` | ADMIN | All orders |

Access control is enforced in `SecurityConfig`: anything not listed there is
denied by default, so new endpoints are protected until they are added
explicitly.

## Authentication

Requests carry a JWT issued by `/api/auth/login` (or `/register`), e.g.:

```
Authorization: Bearer eyJhbGciOi...
```

Passwords are stored as BCrypt hashes; roles (`ADMIN`, `CUSTOMER`) are embedded
in the token and checked by the JWT filter. Tokens expire after 24 h
(`app.jwt.expiration-ms`); an expired/invalid token simply behaves as an
unauthenticated request (401/403).

Example with curl:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@lumiere.com","password":"Customer123!"}' | jq -r .token)

curl -s http://localhost:8080/api/orders/my -H "Authorization: Bearer $TOKEN"
```

## Error format

All errors use one shape (`ErrorResponse`):

```json
{ "timestamp": "...", "status": 400, "message": "Validation failed",
  "fieldErrors": { "email": "must be a valid email address" } }
```

Mapped: `404` unknown id, `409` duplicate email/email in use, `400`
validation/stock issues, `401` missing or wrong credentials, `403` missing role.

## Folder layout

```
backend/src/main/java/com/lumierebeauty/api/
├── common/    error responses + exception handler
├── config/    DataSeeder (demo products + demo accounts)
├── order/     orders, order items, customer + admin controllers
├── product/   products + brands
├── security/  JWT service, filter, security rules, CORS
└── user/      users, roles, register/login/me
```

## Decisions taken (open questions from ../docs/API_REQUIREMENTS.md)

1. **Deletion** — hard delete of the product row.
2. **Stock** — reduced when the order is placed (`POST /api/orders`), not at payment.
3. **Required fields** — name, brand, category, price, stock; `description`,
   `imageUrl` optional; `status` defaults to `Active`, `featured` to `false`.
4. **Browsing** — public without login, including Draft products (front-end
   admin needs them); purchases and account endpoints require login.
5. **Brands** — modelled as product data, exposed via `/api/brands`.
6. **Prices** — single price per product (no discounts/sale prices yet).
7. **Validation/errors** — one error JSON shape for every endpoint (above).

## Notes for production

- Swap H2 for MySQL/PostgreSQL in `application.properties` (comments in that file).
- Move `app.jwt.secret` to an environment variable / secrets manager.
- Turn on tests (`./mvnw test`) before changing logic.
