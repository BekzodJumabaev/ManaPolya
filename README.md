# Manapolya — Sports Field Booking Platform

A full-stack backend application for booking sports fields, built with Spring Boot. Manapolya connects field owners with customers through a public web interface and a secured REST API, handling everything from search and booking to payment-window enforcement and automated booking lifecycle management.

## Features

- **Dual authentication**: stateless JWT for the REST API, session-based authentication for the server-rendered web UI — running side by side in one application.
- **Dynamic search & filtering**: find sports fields by name, district, and region using the JPA Criteria API (Specifications), without writing repetitive repository methods.
- **Booking conflict prevention**: custom JPQL queries validate time-overlaps and enforce business-hour constraints before a booking is created.
- **Automated lifecycle management**: a scheduled job (`@Scheduled`) automatically marks past bookings as completed.
- **Role-based access control**: separate permissions for Admin, Field Owner, and Customer roles.
- **Image upload**: field owners can upload photos for their listings, stored with UUID-based filenames to avoid collisions.
- **Centralized error handling**: a global exception handler returns structured JSON errors for the API and view-based errors for the web UI.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot, Spring MVC |
| Security | Spring Security (JWT + Session) |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Mapping | MapStruct |
| Templating | Thymeleaf |
| Build Tool | Maven |

## Architecture

The project follows a layered architecture:
Each resource has a dedicated DTO and MapStruct mapper, keeping persistence entities decoupled from the data exposed through the API and web views.

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL running locally (or accessible via connection URL)

### Setup

1. Clone the repository
```bash
   git clone https://github.com/BekzodJumabaev/Manapolya.git
   cd Manapolya
```

2. Create a PostgreSQL database and set your connection details as environment variables:
```bash
   export DB_PASSWORD=your_postgres_password
```

3. Update `src/main/resources/application.properties` if your database name, username, or port differ from the defaults.

4. Run the application:
```bash
   mvn spring-boot:run
```

5. The application will be available at `http://localhost:8080`.

## What I Learned

This project was my deep dive into combining two very different authentication models in a single Spring Security configuration — using `securityMatcher` and ordered `SecurityFilterChain` beans to run stateless JWT auth for the API and session-based auth for the web UI without conflicts. It also pushed me to think carefully about data integrity: preventing double-bookings required writing and testing custom JPQL overlap queries inside transactional service methods.

## License

This project was built as part of my studies at PDP Academy (Java Backend track) and is available for educational reference.