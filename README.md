# 💰 SpendWise AI

<p align="center">
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/TypeScript-5-3178C6?style=for-the-badge&logo=typescript&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/AI-Gemini-8E75B2?style=for-the-badge&logo=google&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</p>

<p align="center">
  <b>AI-Powered Personal Finance Management Platform</b>
</p>

<p align="center">
  Track expenses • Manage budgets • Analyze spending • Generate AI insights • Get personalized recommendations
</p>

<p align="center">
  <a href="#-overview">Overview</a> •
  <a href="#-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-tech-stack">Tech Stack</a> •
  <a href="#-api-overview">API</a> •
  <a href="#-installation">Installation</a> •
  <a href="#-docker-setup">Docker</a>
</p>

---

## 📌 Overview

**SpendWise AI** is a full-stack, AI-powered personal finance management application designed to help users understand, manage, and improve their financial habits.

The platform combines a modern **React + TypeScript frontend** with a secure **Java Spring Boot backend**, **PostgreSQL database**, and **Google Gemini AI integration** to provide intelligent financial insights and personalized recommendations.

Users can manage their transactions, create budgets, analyze spending patterns, generate monthly financial reports, and interact with an AI-powered financial assistant.

The application is designed with a scalable architecture that separates the frontend, backend, database, authentication, and AI services.

---

## 🎯 Project Objectives

SpendWise AI aims to simplify personal financial management by providing users with a centralized platform to:

* Track and manage daily transactions.
* Monitor income and expenses.
* Create and manage personal budgets.
* Analyze spending patterns.
* Visualize financial trends.
* Receive AI-powered financial insights.
* Generate monthly financial reports.
* Get personalized budget recommendations.
* Interact with an AI financial assistant.

---

## ✨ Key Features

### 🔐 Secure Authentication

SpendWise AI implements secure authentication using **JWT-based authorization**.

**Features include:**

* User registration
* User login
* Demo authentication endpoints
* JWT token generation
* Secure protected routes
* Current user profile
* Backend authentication and authorization
* Password-based authentication flow

```text
User
  │
  ▼
Register / Login
  │
  ▼
Spring Security
  │
  ▼
JWT Token
  │
  ▼
Authenticated Requests
  │
  ▼
Protected REST APIs
```

---

### 💳 Transaction Management

Users can manage their financial transactions from a centralized dashboard.

**Supported operations:**

* Create transactions
* View transactions
* Update transactions
* Delete transactions
* Track income
* Track expenses
* Categorize spending
* Export transaction data

The transaction system forms the foundation of the application's analytics and AI-powered financial insights.

---

### 💰 Budget Management

Users can create and manage budgets to control their spending.

**Features:**

* Create budgets
* View budgets
* Delete budgets
* Monitor budget progress
* Compare spending against budget limits
* Track budget utilization

Example workflow:

```text
Create Budget
      │
      ▼
Set Spending Limit
      │
      ▼
Track Transactions
      │
      ▼
Calculate Progress
      │
      ▼
View Budget Status
```

---

### 📊 Financial Dashboard

The dashboard provides users with a centralized overview of their financial activity.

**Dashboard analytics include:**

* Total income
* Total expenses
* Current balance
* Recent transactions
* Expense breakdown
* Spending categories
* Monthly financial trends
* Budget progress

The frontend uses **Recharts** to visualize financial information through interactive charts.

---

### 📈 Expense Analytics

SpendWise AI analyzes transaction data to help users understand where their money is going.

Analytics include:

* Expense by category
* Monthly spending trends
* Income vs. expense analysis
* Recent transaction activity
* Budget utilization

These insights allow users to identify spending patterns and make more informed financial decisions.

---

### 🤖 AI-Powered Financial Insights

The application integrates **Google Gemini AI** to provide intelligent financial insights based on user data.

The AI layer can assist with:

* Spending analysis
* Financial insights
* Personalized recommendations
* Monthly financial reports
* Budget recommendations
* Financial question answering

The AI integration is designed to transform raw financial data into useful, understandable recommendations.

```text
Financial Data
      │
      ▼
Transaction & Budget Analysis
      │
      ▼
AI Processing
      │
      ▼
Gemini AI
      │
      ▼
Personalized Insights
      │
      ├── Spending Insights
      ├── Monthly Report
      ├── Budget Recommendations
      └── AI Chat Assistant
```

---

### 📑 Monthly Financial Reports

Users can generate monthly financial reports based on their transaction and budget activity.

Reports can provide insights into:

* Monthly income
* Monthly expenses
* Spending categories
* Budget performance
* Financial trends
* AI-generated observations

---

### 💡 AI Budget Recommendations

SpendWise AI can provide personalized budget recommendations based on financial activity.

The recommendation system can help users:

* Identify high-spending categories.
* Understand spending behavior.
* Adjust budget allocations.
* Set more realistic spending limits.
* Improve financial planning.

---

### 💬 AI Financial Assistant

The application includes an AI-powered chat assistant that allows users to interact with their financial data through natural language.

Users can ask questions related to:

* Spending patterns
* Budget management
* Financial summaries
* Expense categories
* Monthly activity

The assistant provides an intuitive conversational interface for exploring financial information.

---

### 📤 Transaction Export

Users can export transaction data for external analysis, reporting, or record keeping.

This feature provides greater flexibility for managing personal financial records.

---

## 🏗️ System Architecture

SpendWise AI follows a modern full-stack architecture with clear separation between the frontend, backend, database, and AI services.

```text
                         ┌──────────────────────┐
                         │       User           │
                         └──────────┬───────────┘
                                    │
                                    ▼
                    ┌───────────────────────────┐
                    │     React Frontend        │
                    │   TypeScript + Vite       │
                    │                           │
                    │ TanStack Router           │
                    │ TanStack Query            │
                    │ Tailwind CSS               │
                    │ Radix UI                   │
                    │ Recharts                   │
                    └─────────────┬─────────────┘
                                  │
                           REST API Requests
                                  │
                                  ▼
                    ┌───────────────────────────┐
                    │    Spring Boot Backend    │
                    │         Java 21           │
                    │                           │
                    │ Spring Web                │
                    │ Spring Security           │
                    │ Spring Data JPA            │
                    │ Spring Validation         │
                    │ JWT Authentication       │
                    └───────┬───────────┬───────┘
                            │           │
                            │           │
                            ▼           ▼
                 ┌──────────────┐  ┌──────────────┐
                 │ PostgreSQL   │  │  Gemini AI   │
                 │  Database    │  │   Service    │
                 └──────────────┘  └──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │    Flyway    │
                    │  Migrations  │
                    └──────────────┘
```

---

## 🔄 Application Data Flow

```text
                  User
                   │
                   ▼
            React Frontend
                   │
                   │ REST API
                   ▼
          Spring Boot Backend
                   │
          ┌────────┴────────┐
          │                 │
          ▼                 ▼
      PostgreSQL        Gemini AI
          │                 │
          │                 │
          └────────┬────────┘
                   │
                   ▼
            Business Logic
                   │
                   ▼
            API Response
                   │
                   ▼
            React Dashboard
```

---

## 🛠️ Tech Stack

### 🎨 Frontend

| Technology      | Purpose                                       |
| --------------- | --------------------------------------------- |
| React 19        | Frontend UI development                       |
| TypeScript      | Type-safe application development             |
| Vite            | Fast development and build tooling            |
| TanStack Query  | Server-state management and API data fetching |
| TanStack Router | Type-safe client-side routing                 |
| Tailwind CSS v4 | Utility-first styling                         |
| Radix UI        | Accessible UI primitives                      |
| Recharts        | Financial data visualization                  |
| Lucide React    | Icon library                                  |
| React Hook Form | Form management                               |
| Zod             | Schema validation                             |

---

### ⚙️ Backend

| Technology        | Purpose                          |
| ----------------- | -------------------------------- |
| Java 21           | Backend programming language     |
| Spring Boot 3.4.2 | Backend application framework    |
| Spring Web        | REST API development             |
| Spring Data JPA   | Database access and ORM          |
| Spring Security   | Authentication and authorization |
| Spring Validation | Request and data validation      |
| JWT               | Stateless authentication         |
| PostgreSQL Driver | PostgreSQL connectivity          |
| H2                | In-memory database fallback      |
| Flyway            | Database migration management    |
| Springdoc OpenAPI | API documentation                |
| Swagger UI        | Interactive API documentation    |

---

### 🤖 AI

| Technology    | Purpose                                 |
| ------------- | --------------------------------------- |
| Google Gemini | AI-powered financial analysis           |
| Gemini API    | AI insights and assistant functionality |

---

### 🐳 DevOps & Infrastructure

| Technology     | Purpose                                 |
| -------------- | --------------------------------------- |
| Docker         | Application containerization            |
| Docker Compose | Multi-container orchestration           |
| PostgreSQL     | Production database                     |
| H2             | Development/testing database fallback   |
| Maven          | Backend dependency and build management |
| npm            | Frontend package management             |

---

## 📂 Project Structure

```text
SpendWise-AI/
│
├── backend/
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── .../
│   │   │   │       ├── controller/
│   │   │   │       │   ├── AuthController
│   │   │   │       │   ├── TransactionController
│   │   │   │       │   ├── BudgetController
│   │   │   │       │   ├── DashboardController
│   │   │   │       │   └── AIController
│   │   │   │       │
│   │   │   │       ├── service/
│   │   │   │       │   └── Business Logic
│   │   │   │       │
│   │   │   │       ├── security/
│   │   │   │       │   └── JWT & Spring Security
│   │   │   │       │
│   │   │   │       └── ai/
│   │   │   │           └── Gemini Integration
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   │
│   │   ├── pom.xml
│   │   └── mvnw
│   │
│   ├── frontend/
│   │   │
│   │   ├── routes/
│   │   │   └── Application Routes
│   │   │
│   │   ├── components/
│   │   │   └── Reusable UI Components
│   │   │
│   │   ├── api/
│   │   │   └── API Client Wrappers
│   │   │
│   │   ├── lib/
│   │   │   └── Utilities & Helpers
│   │   │
│   │   ├── router.tsx
│   │   └── routeTree.gen.ts
│   │
│   ├── package.json
│   └── ...
│
├── docker-compose.yml
├── README.md
└── ...
```

> **Note:** The exact directory structure may vary depending on the current implementation.

---

## 🔌 API Overview

The backend exposes RESTful APIs for the application's core functionality.

### 🔐 Authentication APIs

```text
POST   /api/auth/register
POST   /api/auth/login
GET    /api/auth/demo
GET    /api/auth/me
```

---

### 💳 Transaction APIs

```text
POST   /api/transactions
GET    /api/transactions
PUT    /api/transactions/{id}
DELETE /api/transactions/{id}
GET    /api/transactions/export
```

---

### 💰 Budget APIs

```text
POST   /api/budgets
GET    /api/budgets
DELETE /api/budgets/{id}
GET    /api/budgets/progress
```

---

### 📊 Dashboard & Analytics APIs

```text
GET /api/dashboard/summary
GET /api/dashboard/recent-transactions
GET /api/dashboard/expense-by-category
GET /api/dashboard/monthly-trend
```

---

### 🤖 AI APIs

```text
GET  /api/ai/insights
GET  /api/ai/monthly-report
GET  /api/ai/budget-recommendation
POST /api/ai/chat
```

> **Note:** API paths may differ based on the final controller mappings in the implementation. Use the generated Swagger/OpenAPI documentation for the exact endpoint definitions.

---

## 📖 API Documentation

SpendWise AI uses **Springdoc OpenAPI** to provide interactive API documentation.

After starting the backend, access the Swagger UI through the configured Swagger endpoint.

Swagger can be used to:

* Explore available REST APIs.
* View request and response schemas.
* Test endpoints.
* Review authentication requirements.
* Understand API parameters.

---

## 🔐 Environment Variables

### Backend

The backend supports the following environment variables:

| Variable               | Description                            |
| ---------------------- | -------------------------------------- |
| `DATABASE_URL`         | PostgreSQL database connection URL     |
| `DATABASE_USERNAME`    | PostgreSQL username                    |
| `DATABASE_PASSWORD`    | PostgreSQL password                    |
| `JWT_SECRET`           | Secret key used for JWT authentication |
| `GEMINI_API_KEY`       | Google Gemini API key                  |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins               |

Example:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/spendwise_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgrespassword
JWT_SECRET=your-secure-jwt-secret
GEMINI_API_KEY=your-gemini-api-key
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

### Frontend

```env
VITE_API_BASE_URL=http://localhost:8888
```

> **Security:** Never commit `.env` files or production secrets to GitHub. Use environment variables or secure secret management solutions.

---

# 🚀 Installation & Local Development

## Prerequisites

Make sure you have the following installed:

* Node.js
* npm
* Java 21
* Maven or Maven Wrapper
* PostgreSQL
* Git

Optional:

* Docker
* Docker Compose

---

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/SpendWise-AI.git
cd SpendWise-AI
```

---

## 2️⃣ Start the Backend

Navigate to the backend directory:

```bash
cd backend
```

Run the Spring Boot application:

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8888
```

---

## 3️⃣ Start the Frontend

Open a new terminal and navigate to the frontend directory.

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend will typically be available at:

```text
http://localhost:5173
```

Configure the backend API URL if required:

```env
VITE_API_BASE_URL=http://localhost:8888
```

---

## 🐳 Docker Compose Setup

SpendWise AI includes a `docker-compose.yml` configuration for running the application infrastructure using containers.

The Docker setup includes:

```text
┌──────────────────────────┐
│      Docker Compose      │
└────────────┬─────────────┘
             │
      ┌──────┴──────┐
      │             │
      ▼             ▼
 PostgreSQL     Spring Boot
  Database       Backend
      │             │
      └──────┬──────┘
             │
             ▼
        REST API
             │
             ▼
       React Frontend
```

### Start Containers

```bash
docker-compose up -d
```

### Stop Containers

```bash
docker-compose down
```

### View Logs

```bash
docker-compose logs -f
```

### Default PostgreSQL Configuration

```text
Database: spendwise_db
Username: postgres
Password: postgrespassword
```

The backend container exposes:

```text
localhost:8888
```

> For production deployments, replace default credentials with secure environment-specific values.

---

## 🏗️ Build the Application

### Frontend

Install dependencies:

```bash
npm install
```

Create a production build:

```bash
npm run build
```

---

### Backend

Build the Spring Boot application:

```bash
cd backend
./mvnw clean package
```

Run the generated application:

```bash
java -jar target/*.jar
```

---

## 🗄️ Database Management

SpendWise AI uses **PostgreSQL** as its primary production database.

**Spring Data JPA** handles object-relational mapping, while **Flyway** manages database schema migrations.

For development or testing, the application configuration also supports **H2** as an in-memory database fallback.

```text
Production
    │
    ▼
PostgreSQL
    │
    ▼
Spring Data JPA
    │
    ▼
Flyway Migrations

Development / Testing
    │
    ▼
H2 In-Memory Database
```

---

## 🔄 Application Workflow

```text
                  ┌───────────────┐
                  │     User      │
                  └───────┬───────┘
                          │
                          ▼
                  Login / Register
                          │
                          ▼
                    JWT Token
                          │
                          ▼
                  Personal Dashboard
                          │
           ┌──────────────┼──────────────┐
           │              │              │
           ▼              ▼              ▼
      Transactions     Budgets       Analytics
           │              │              │
           └──────────────┼──────────────┘
                          │
                          ▼
                    Financial Data
                          │
                          ▼
                       AI Layer
                          │
           ┌──────────────┼──────────────┐
           │              │              │
           ▼              ▼              ▼
        Insights     Recommendations   Reports
           │              │              │
           └──────────────┼──────────────┘
                          │
                          ▼
                   AI Chat Assistant
```

---

## 📊 Frontend Dashboard

The frontend provides an interactive financial dashboard where users can visualize their financial health.

Potential dashboard components include:

* 💰 Balance overview
* 📈 Income and expense trends
* 💳 Recent transactions
* 🥧 Expense category breakdown
* 🎯 Budget progress
* 🤖 AI-generated insights
* 💡 Budget recommendations

The interface is built using a modern component-based architecture with **React 19**, **TypeScript**, **Tailwind CSS**, and **Radix UI**.

---

## 🔒 Security

SpendWise AI uses several security mechanisms to protect user data and application resources.

### Authentication

* JWT-based stateless authentication
* Spring Security integration
* Protected backend routes
* Token-based API authorization

### Data Protection

* Environment-based configuration
* Database credentials externalized through environment variables
* API keys managed through environment variables
* CORS configuration
* Input validation using Spring Validation
* Secure database access through Spring Data JPA

> Production deployments should use HTTPS, strong JWT secrets, secure database credentials, and a properly configured CORS policy.

---

## 🧪 Development & Testing

The project can be tested across different layers:

### Frontend

```bash
npm run build
```

### Backend

```bash
cd backend
./mvnw clean package
```

### API Testing

Use Swagger UI or API clients such as Postman to test:

* Authentication
* Transaction CRUD
* Budget operations
* Dashboard analytics
* AI endpoints

---
