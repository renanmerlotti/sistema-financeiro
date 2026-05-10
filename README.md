# Personal Finance Manager 

A full-stack personal finance management app built with Spring Boot and React. Users can track accounts, categorize transactions, and monitor income vs. expenses through a dashboard with charts and period filtering.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-6DB33F?style=flat&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=flat&logo=react&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat&logo=docker&logoColor=white)

---

## Demo

Video

---

## Features

### Auth
- Register and login with email/password
- JWT returned on success, stored in `localStorage`
- All subsequent requests authenticated via `Authorization: Bearer <token>`

### Accounts
- Create accounts by type: checking, savings, or wallet
- List, edit, and delete accounts
- Account balances reflected on the dashboard

### Transactions
- Create income and expense transactions linked to an account and category
- Paginated listing with optional date range filter (`startDate` / `endDate`)
- Edit and delete any transaction

### Categories
- User-defined categories typed as income or expense
- Categories are referenced when creating transactions and used to compute spending breakdowns

### Dashboard
- Aggregated summary: total balance, total income, total expenses, savings rate
- Daily cashflow chart (income vs. expenses over the selected period)
- Spending breakdown by category
- Recent transactions and accounts list
- Period-driven — all data recalculated server-side per request

---

## Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Runtime | Java | 21 |
| Framework | Spring Boot | 3.4.4 |
| Persistence | Spring Data JPA + MySQL | 8.0 |
| Migrations | Flyway | — |
| Security | Spring Security + JWT | 0.12.6 |
| Frontend | React + Vite | 19 / 8 |
| Styling | Tailwind CSS | 4 |
| Charts | Recharts | 3 |
| Containers | Docker + Compose | — |

---

## Running Locally

Requirements: Docker and Docker Compose.

```bash
git clone https://github.com/renanmerlotti/sistema-financeiro
cd sistema-financeiro
docker compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |
| MySQL | localhost:3306 |

The database starts first (with a health check), then the backend, then the frontend. Flyway runs migrations automatically on backend startup — no manual schema setup needed.

---

## Project Structure

```
sistema-financeiro/
├── backend/
│   ├── src/main/java/com/financas/backend/
│   │   ├── controller/       # REST endpoints
│   │   ├── service/          # Business logic (interface + impl)
│   │   ├── repository/       # Spring Data JPA repositories
│   │   ├── entity/           # JPA entities and enums
│   │   ├── dto/              # Request/response DTOs + mappers
│   │   ├── security/         # JWT filter, JwtService, UserDetailsService
│   │   ├── exception/        # Domain exceptions
│   │   └── config/           # SecurityConfig
│   └── src/main/resources/
│       └── db/migration/     # Flyway SQL scripts (V1–V4)
├── frontend/
│   └── src/
│       ├── api/              # Axios instance
│       ├── context/          # AuthContext
│       └── components/
│           ├── layout/       # Sidebar, Header
│           ├── dashboard/    # Charts and summary widgets
│           ├── accounts/     # Account cards and slide-over form
│           ├── categories/   # Category slide-over form
│           └── transactions/ # Transaction slide-over form
└── docker-compose.yaml
```

---

## Technical Decisions

**Flyway over `ddl-auto=update`**
`ddl-auto=update` silently drops columns and makes unsafe schema changes. Flyway enforces append-only, versioned migrations that are deterministic, auditable, and safe to run in any environment. Each migration file is immutable once applied.

**JWT stateless auth**
No server-side sessions means no shared state between instances. The backend validates the token signature on every request and extracts the user identity directly from the claims — `@AuthenticationPrincipal` injects the resolved `User` entity into controllers. This makes the API horizontally scalable by default.

**Multi-stage Docker builds**
The backend Dockerfile uses a two-stage build: a JDK image to compile and package the JAR, then a JRE-only image to run it. The build toolchain never ends up in the final image. The frontend runs the dev server inside a Node container, which keeps the setup simple and self-contained without requiring a local Node install.

**Per-user data isolation**
Every service method receives the authenticated user's ID (extracted from the JWT principal) and appends it to every query. No data is global — accounts, categories, and transactions are fully scoped to the authenticated user.

---

## What I Learned

- **Spring Security filter chain**: Wiring a custom `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter` and configuring stateless session management required understanding the filter execution order and how `SecurityContext` is populated per request.

- **Database migration discipline**: Treating schema changes as append-only, versioned artifacts forces you to think about data migration explicitly rather than letting the ORM silently alter production tables.

- **Docker Compose health checks**: Relying on `depends_on` alone doesn't guarantee a service is ready — the MySQL container needs a health check so the backend doesn't attempt to run Flyway migrations against a database that isn't accepting connections yet.

- **Recharts composition model**: Building the cashflow and spending charts by composing `<ComposedChart>`, `<Bar>`, `<Line>`, and `<Tooltip>` components made customization straightforward, though getting responsive containers to behave correctly inside flex layouts took iteration.

- **Unit testing with JUnit 5 and Mockito**: Writing tests for the service layer required understanding how to isolate business logic by mocking repository dependencies with Mockito. The turning point was shifting focus from memorizing syntax to analyzing each method's dependencies before writing any assertion.
