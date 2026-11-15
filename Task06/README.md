# Task 6 – Campus Event Registration App

## Architecture

```text
CampusEventApp (Swing UI)
        |
        v
RegistrationService (validation and business rules)
        |
        v
RegistrationDAO (JDBC and SQL)
        |
        v
MySQL: event / registration
```

Database calls run in `SwingWorker` background tasks. Table data is loaded page-by-page using `LIMIT` and `OFFSET`. A unique database constraint on `(student_id, event_id)` prevents duplicate registrations even if two requests race.

## Setup

1. Run `schema.sql` in MySQL.
2. Download MySQL Connector/J.
3. Set `DB_URL`, `DB_USER`, and `DB_PASSWORD`.
4. Compile with the connector JAR on the classpath.

Example:

```bash
javac -cp ".:mysql-connector-j.jar" Task06/CampusEventApp.java
java -cp "Task06:mysql-connector-j.jar" CampusEventApp
```

On Windows, replace `:` in the classpath with `;`.

Default connection URL if `DB_URL` is not set:

`jdbc:mysql://localhost:3306/campus_events`

Never commit database passwords to the repository.
