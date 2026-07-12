# Aynak — Civic Reporting Platform
Aynak is web application for reporting civic issues in the UAE. It lets citizens report issues such as street garbage, broken lights, injured/dead animal on the street. It enables residents to report issues using AI-powered image recognition. It then tracks the reported issues through a review workflow with local authorities, and rewards civic participation through a points/voucher system sponsored by local businesses.

## Table of Contents
- [System Architecture](#system-architecture)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup & Run Guide](#setup--run-guide)
- [Environment Variables](#environment-variables)
- [Troubleshooting](#troubleshooting)

## System Architecture

<p align="center">
<img src="docs/architecture.png" alt="Aynak System Architecture" width="900">
</p>

- The API Gateway only routes requests — each microservice independently validates the JWT against Keycloak.
- Services register with Eureka so the gateway and services can find each other without hardcoded URLs.

## Tech Stack

**Backend**
- Java 21, Spring Boot 4.0.6, Spring Cloud 2025.1.1
- Spring Cloud Gateway (WebMVC), Netflix Eureka, OpenFeign
- Spring Security OAuth2 Resource Server (JWT validation against Keycloak)
- Spring Data JPA + MySQL
- Google Gemini API — AI-assisted report analysis
- Jakarta Mail (Gmail SMTP) — email notifications

**Frontend**
- React 19, Vite 8, React Router 7
- `keycloak-js` — OIDC/PKCE authentication
- Mapbox GL — interactive maps for report locations
- Recharts — analytics dashboards

**Identity & Infra**
- Keycloak (realm-based OIDC provider, Google login federation, role-based access)
- MySQL 8
- Docker (Keycloak container)

## Features

**Citizen**
- Submit civic issue reports with photos and location
- AI-assisted report analysis via Gemini
- View, track, and delete their own reports
- Earn points, view the leaderboard and their own rank
- Redeem points for sponsor-provided rewards/vouchers
- In-app notifications
- Profile management

**Authority**
- Review pending reports; approve, reject, mark in-progress, or resolved
- View all approved reports
- Category/status analytics dashboards

**Sponsor**
- Create and manage reward vouchers

**Admin**
- Elevated access across report management

**Cross-cutting**
- Google login via Keycloak, alongside username/password
- Role-based access on both frontend and backend

## Prerequisites

- Java 21 (each backend module includes the Maven Wrapper, so a local Maven install isn't required)
- Node.js 18+ and npm
- MySQL 8 running locally on port `3306`
- Docker Desktop (for Keycloak)
- A Google Gemini API key
- A Gmail account with an **App Password** generated (not your normal Gmail password)
- A Mapbox account/public access token

## Setup & Run Guide

### 1. Create the MySQL databases

```sql
CREATE DATABASE user2_db;
CREATE DATABASE civic2_db;
```

Both services connect as `root` to `localhost:3306`; tables are created automatically on first run.

### 2. Start Keycloak in Docker

```bash
docker volume create keycloak2_data

docker run -d ^
  --name keycloakcontain2 ^
  -p 8082:8080 ^
  -v keycloak2_data:/opt/keycloak/data ^
  -e KEYCLOAK_ADMIN=sabrina-admin ^
  -e KEYCLOAK_ADMIN_PASSWORD=admin ^
  quay.io/keycloak/keycloak:latest ^
  start-dev
```

> The `^` line continuations are for Windows CMD. On macOS/Linux, replace `^` with `\`.
>
> Change the admin username/password if this is ever exposed beyond your machine.

### 3. Import the realm

1. Open `http://localhost:8082` and log in with the admin credentials above.
2. Click the realm dropdown (top-left) → **Create Realm**.
3. Click **Browse**, select `keycloak/realm-export-aynak.json` from this repo, then **Create**.
4. Keycloak strips secrets on export, so re-enter these manually after import:
   - **Identity Providers → google → Client Secret** — your Google OAuth client secret (only needed for "Sign in with Google")
   - **Realm Settings → Email → Password** — your Gmail App Password (only needed for Keycloak's own emails)

### 4. Configure and run the backend services

Set the required environment variables (Windows CMD example):

```bash
set DATABASE_PASSWORD=your_mysql_root_password
set GEMINI_API_KEY=your_gemini_api_key
set GMAIL_APP_PASSWORD=your_gmail_app_password
```

Start each service **in this order**, each in its own terminal, from its own folder:

```bash
cd backend/service-registry
mvnw.cmd spring-boot:run
```
```bash
cd backend/user-service
mvnw.cmd spring-boot:run
```
```bash
cd backend/civic-service
mvnw.cmd spring-boot:run
```
```bash
cd backend/api-gateway
mvnw.cmd spring-boot:run
```

(On macOS/Linux use `./mvnw spring-boot:run`.)

Wait for `service-registry` (port `8761`) to be up before starting the others.

### 5. Run the frontend

```bash
cd frontend
npm install
```

Create a `.env` file inside `frontend/`:

```env
VITE_MAPBOX_TOKEN=your_mapbox_public_token
VITE_KEYCLOAK_URL=http://localhost:8082
VITE_KEYCLOAK_REALM=aynak
VITE_KEYCLOAK_CLIENT_ID=aynak-spa
VITE_API_BASE_URL=http://localhost:8765
```

```bash
npm run dev
```

Open `http://localhost:5173`.

> The API Gateway's CORS config only allows `http://localhost:5173` as an origin. If you run the frontend on a different port, update `CorsConfig.java` in `api-gateway`.

## Environment Variables

| Variable | Used by | Description |
|---|---|---|
| `DATABASE_PASSWORD` | user-service, civic-service | MySQL `root` password |
| `GEMINI_API_KEY` | civic-service | Google Gemini API key for AI report analysis |
| `GMAIL_APP_PASSWORD` | civic-service | Gmail App Password for `aynaksupport@gmail.com` |
| `VITE_MAPBOX_TOKEN` | frontend | Mapbox public access token |
| `VITE_KEYCLOAK_URL` | frontend | Keycloak base URL (`http://localhost:8082`) |
| `VITE_KEYCLOAK_REALM` | frontend | Keycloak realm (`aynak`) |
| `VITE_KEYCLOAK_CLIENT_ID` | frontend | Keycloak SPA client (`aynak-spa`) |
| `VITE_API_BASE_URL` | frontend | API Gateway URL (`http://localhost:8765`) |

## Troubleshooting

- **Keycloak login not completing on Windows:** Open **Control Panel → Internet Options → Advanced tab → Security section**, and uncheck **"Check for server certificate revocation"**.
- **Gateway returns 404 / can't reach a service:** Confirm `service-registry` is running first and check its dashboard at `http://localhost:8761`.
- **401/403 from the backend:** Confirm your Keycloak realm's issuer matches `http://localhost:8082/realms/aynak`, and that you re-entered the Google IdP secret if using Google login.

