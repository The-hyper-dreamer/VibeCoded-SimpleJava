# Classroom Maintenance Request System

A Java Swing desktop application for managing classroom maintenance requests, connected to a MySQL database via JDBC.

## Project Structure

```
ClassroomMaintenance/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/anex/maintenance/
│       │   ├── Main.java
│       │   ├── model/
│       │   │   ├── User.java
│       │   │   └── MaintenanceRequest.java
│       │   ├── dao/
│       │   │   ├── UserDAO.java
│       │   │   └── MaintenanceRequestDAO.java
│       │   ├── service/
│       │   │   ├── AuthenticationService.java
│       │   │   └── RequestService.java
│       │   └── ui/
│       │       ├── LoginFrame.java
│       │       ├── FacultyDashboard.java
│       │       ├── HODDashboard.java
│       │       └── (Request details shown inline)
│       └── resources/
│           └── db.properties.example
├── sql/
│   └── schema.sql
└── .gitignore
```

## Prerequisites

- Java JDK 21 or higher
- Apache Maven 3.6+
- MySQL Community Server 8.0+
- MySQL Connector/J 8.0.33 (included via Maven)

## Setup

### 1. Create the database

Run the schema script in MySQL Workbench or via command line:

```bash
mysql -u root -p -P 3307 < sql/schema.sql
```

### 2. Create a dedicated application user (recommended)

```sql
CREATE USER 'maintenance_app'@'localhost' IDENTIFIED BY 'your_app_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON anex.* TO 'maintenance_app'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure database connection

Copy `src/main/resources/db.properties.example` to `db.properties` in the project root and update credentials:

```properties
db.url=jdbc:mysql://localhost:3307/anex
db.username=maintenance_app
db.password=your_app_password
```

### 4. Insert demo accounts

Run these SQL commands to create demo accounts (passwords are SHA-256 hashed):

```sql
-- Faculty: faculty1 / faculty123
-- HOD: hod1 / hod123
INSERT INTO users (username, password_hash, role, active) VALUES
('faculty1', SHA2('faculty123', 256), 'FACULTY', TRUE),
('hod1', SHA2('hod123', 256), 'HOD', TRUE);
```

## Build and Run

### Build with Maven

```bash
mvn clean package
```

### Run with Maven

```bash
mvn exec:java -Dexec.mainClass="com.iesce.maintenance.Main"
```

Or run the JAR directly:

```bash
java -cp target/classroom-maintenance-1.0.0.jar;target/lib/* com.iesce.maintenance.Main
```

## Demo Credentials

| Role | Username | Password |
|------|----------|----------|
| Faculty | faculty1 | faculty123 |
| HOD | hod1 | hod123 |

## Features

- Login with role-based access (Faculty / HOD)
- Faculty can submit maintenance requests
- Faculty dashboard shows PENDING, APPROVED, REJECTED (latest 2), COMPLETED (latest 2) requests
- HOD can view all requests, approve, reject, add remarks, and mark as completed
- All data persisted in MySQL via JDBC
- Passwords hashed with SHA-256

## Database Connection Test

The application connects to MySQL on startup. If the database is unavailable, a clear error message is shown.

## Local Network Setup

For multiple computers on a LAN:
1. Host MySQL on a central machine
2. Bind MySQL to the host's LAN IP (not just localhost)
3. Create the app user with `%` or specific IP grants
4. Configure Windows Firewall to allow port 3307
5. Update `db.properties` on client machines with the host's IP

## License

Educational project for college coursework.
