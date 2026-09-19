
# TaskBoard - Personal Task and Label Management API

A RESTful task management API built with **Java 21 and Spring Boot**. TaskBoard provides secure user authentication, task and label management, dynamic filtering, pagination, CSV export, scheduled reminders, and soft deletion.

The project was developed as part of the **DevLab Intern-Acceleration Program** with a focus on clean architecture, secure API design, database management, and production-oriented backend development.

---

## Features

- User registration and authentication
- JWT-based stateless authentication
- BCrypt password hashing
- User-owned tasks and labels
- Full CRUD operations for tasks and labels
- Task statuses:
  - `TODO`
  - `IN_PROGRESS`
  - `BLOCKED`
  - `DONE`
- Multiple labels per task
- Dynamic task filtering
- Keyword search
- Sorting and pagination
- Deadline filtering
- Ownership-based authorization
- Bean Validation
- Global exception handling
- Database migrations with Flyway
- Database indexes for frequently queried fields
- Soft deletion for tasks and labels
- CSV export of completed tasks
- Scheduled task deadline reminders
- OpenAPI / Swagger documentation

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.1.1 | Application framework |
| Spring Security 7 | Authentication & authorization |
| JJWT 0.13.0 | JWT generation and validation |
| Spring Data JPA | Persistence layer |
| Hibernate ORM 7 | ORM |
| PostgreSQL 16 | Relational database |
| Flyway | Database migrations |
| MapStruct 1.6.3 | DTO mapping |
| Lombok | Boilerplate reduction |
| Jakarta Bean Validation | Request validation |
| SpringDoc OpenAPI 3.1.0 | API documentation |
| Gradle | Build automation |

---

Additional components handle authentication, validation, dynamic filtering, scheduled jobs, DTO mapping, and exception handling.

---

## Database

TaskBoard uses PostgreSQL with Flyway-managed schema migrations.

### Main Entities

```text
User
 ├── Tasks
 └── Labels

Task
 └── Labels (many-to-many)
```

The database contains:

* `users`
* `tasks`
* `labels`
* `task_labels`

Foreign keys and cascading relationships are used to maintain referential integrity.

### Migrations

```text
V1__init_schema.sql
V2__add_performance_indexes.sql
```

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This allows Hibernate to validate the database schema without modifying it.

---

## Performance

Indexes are added for frequently queried fields:

```text
idx_tasks_user_status
idx_tasks_deadline
idx_task_labels_label_id
```

Task filtering uses **Spring Data JPA Specifications**, allowing multiple filters to be combined dynamically without creating separate repository methods for every possible combination.

Pagination is implemented using Spring Data's `Pageable`.

---

## Getting Started

### Prerequisites

Make sure the following are installed:

* Java 21
* PostgreSQL 16+
* Git

### 1. Clone the repository

```bash
git clone https://github.com/IslamNizami/TaskBoard.git
cd TaskBoard
```

### 2. Create the database

Create a PostgreSQL database:

```sql
CREATE DATABASE taskboard;
```

### 3. Configure environment variables

Set the JWT secret as an environment variable:

```bash
JWT_SECRET=your-secure-secret
```

Configure the database connection in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskboard
spring.datasource.username=your_username
spring.datasource.password=your_password

application.security.jwt.secret-key=${JWT_SECRET}
application.security.jwt.expiration=86400000
```

Do not commit real database credentials or JWT secrets to the repository.

### 4. Run the application

Using Gradle:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

The application runs on:

```text
http://localhost:7070
```

Flyway automatically applies the database migrations during startup.

---

## Authentication

TaskBoard uses JWT-based stateless authentication.

The login endpoint returns a JWT access token.

Authenticated requests use:

```http
Authorization: Bearer <access-token>
```

Passwords are stored using **BCrypt hashing**.

---

## API Endpoints

### Authentication

| Method | Endpoint             | Description         |
| ------ | -------------------- | ------------------- |
| POST   | `/api/auth/register` | Register a user     |
| POST   | `/api/auth/login`    | Authenticate a user |

### Tasks

| Method | Endpoint                | Description            |
| ------ | ----------------------- | ---------------------- |
| POST   | `/api/tasks`            | Create a task          |
| GET    | `/api/tasks`            | Get user's tasks       |
| GET    | `/api/tasks/{id}`       | Get a task             |
| PUT    | `/api/tasks/{id}`       | Update a task          |
| DELETE | `/api/tasks/{id}`       | Soft-delete a task     |
| GET    | `/api/tasks/export/csv` | Export completed tasks |

### Labels

| Method | Endpoint           | Description         |
| ------ | ------------------ | ------------------- |
| POST   | `/api/labels`      | Create a label      |
| GET    | `/api/labels`      | Get user's labels   |
| GET    | `/api/labels/{id}` | Get a label         |
| PUT    | `/api/labels/{id}` | Update a label      |
| DELETE | `/api/labels/{id}` | Soft-delete a label |

---

## Task Filtering

Tasks can be filtered using multiple query parameters.

Supported filters include:

* Status
* Label
* Deadline
* Keyword

Pagination and sorting are also supported.

Example:

```http
GET /api/tasks?status=IN_PROGRESS&labelId=2&page=0&size=10&sort=deadline,asc
```

Keyword search:

```http
GET /api/tasks?keyword=project
```

Multiple filters can be combined in a single request.

---

## Ownership & Authorization

Users can only access their own tasks and labels.

Unauthorized access results in an appropriate HTTP response.

---

## Validation & Error Handling

Incoming requests are validated using Jakarta Bean Validation.

The application provides centralized exception handling through `@RestControllerAdvice`.

---

## Soft Delete

Tasks and labels support soft deletion.

Instead of immediately removing records from the database, the application marks them as deleted.

```text
is_deleted = true
```

Normal queries exclude deleted records.

This preserves historical data while preventing deleted resources from appearing in regular API responses.

---

## CSV Export

Completed tasks can be exported as CSV:

```http
GET /api/tasks/export/csv
```

Only the authenticated user's completed (`DONE`) tasks are exported.

The export is generated as a streamed response and follows CSV escaping rules for values containing commas, quotes, or line breaks.

---

## Scheduled Reminders

TaskBoard includes a scheduled reminder service.

The scheduler runs hourly and checks for non-completed tasks approaching their deadlines.

```text
TaskReminderScheduler
        ↓
Every hour
        ↓
Find upcoming tasks
        ↓
Log reminder information
```

The scheduler checks tasks that:

* Are not deleted
* Are not `DONE`
* Have an upcoming deadline within the configured reminder window

---

## API Documentation

Swagger UI is available while the application is running:

```text
http://localhost:7070/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:7070/v3/api-docs
```

Swagger can be used to explore and test the available endpoints.
---

## License

This project was developed for educational and portfolio purposes as part of the DevLab Intern-Acceleration Program.

```
