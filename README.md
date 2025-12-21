# Library Management System (Swing + JDBC)

A Java SE 8+ Swing application that manages authentication, profile updates, book catalog, and borrowing rules using a layered architecture without external frameworks. MySQL access uses JDBC with the MySQL Connector JAR expected in `lib/mysql-connector-java-8.0.33.jar` (or compatible).

## Structure
- `abstract_`: shared abstract classes for UI and service layers.
- `db`: JDBC connection and schema bootstrap.
- `exception`: business and validation exceptions.
- `interface_`: repository contracts.
- `model`: domain models and role enum.
- `repository`: JDBC implementations for data access.
- `service`: business logic and validations.
- `ui`: Swing screens (login, register, books, profile, book form, main launcher).
- `util`: helpers for hashing and validation.
- `sql/schema.sql`: database schema and sample admin user.
- `lib`: place the MySQL connector JAR manually.

## Database
1. Ensure MySQL is running and create a user with privileges for `library_app`.
2. Update credentials in `db/DatabaseConnection.java` if necessary.
3. Run the SQL in `sql/schema.sql` or rely on the automatic initializer executed at startup.
4. First run seeds `admin / admin123` (hashed) with role ADMIN.

## Running
Compile the sources and include the connector jar in the classpath, for example:

```
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d out $(find src -name "*.java")
java -cp "out:lib/mysql-connector-java-8.0.33.jar" ui.Main
```

