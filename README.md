# Liv2Train — Backend Assignment (Spring Boot + MySQL)

Training Center registry API with **create** and **list** endpoints, validation, and duplicate handling.

> **Repo layout:** the Spring Boot project is inside **`/liv2train`** (this repository’s root also contains IDE/config folders).  
> When running commands below, **cd into** `liv2train/` first.

---

## Tech Stack

- Java **17**
- Spring Boot **3.5.x** (Web, Validation, Data JPA)
- MySQL **8.0**
- Hibernate ORM (HikariCP connection pool)
- Maven Wrapper (`mvnw`, `mvnw.cmd`) – no global Maven install required
- (Optional) Postman for API testing

---

## Requirements

- Windows 10/11, macOS, or Linux
- **JDK 17+** (`java -version` should print 17.x)
- **MySQL 8** running on `localhost:3306`
- Internet access on first run (Maven downloads dependencies)

---

## Getting the Code

### Option A — Download ZIP
1. **Code → Download ZIP** on GitHub, then extract.
2. Open the extracted folder and go into **`liv2train/`** in your IDE or terminal.

### Option B — Clone
```bash
git clone https://github.com/<your-username>/Backend_Liv2Train_SaaiJena.git
cd Backend_Liv2Train_SaaiJena/liv2train
Database Setup (MySQL)
Create the database (Workbench or CLI):

sql

CREATE DATABASE liv2train CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Optional: create a dedicated user for local dev
CREATE USER 'liv2train'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON liv2train.* TO 'liv2train'@'localhost';
FLUSH PRIVILEGES;
Application Configuration
Do not commit real credentials. Keep secrets in your local application.properties.

Go to: liv2train/src/main/resources/

Create application.properties (or copy from application-example.properties if present), and fill:

properties

# --- MySQL datasource ---
spring.datasource.url=jdbc:mysql://localhost:3306/liv2train?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD

# --- JPA / Hibernate ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
# (Boot 3 auto-selects dialect; this line is optional)
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# (optional) Hikari tweaks
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=30000
Run the Application
IntelliJ IDEA
Open the liv2train/ folder as a project.

Wait for Maven to finish indexing/dependencies.

Run Liv2trainApplication (green ▶).

You should see: Tomcat started on port(s): 8080.

Terminal (Maven Wrapper)
bash

# from the liv2train/ folder
./mvnw spring-boot:run          # macOS/Linux
.\mvnw spring-boot:run          # Windows
Base URL: http://localhost:8080

API Endpoints
Create Training Center
POST /api/v1/training-centers

Request (JSON):

json

{
  "centerName": "Alpha Skills",
  "centerCode": "ABC123DEF456",
  "address": {
    "detailedAddress": "12 Park Street",
    "city": "Kolkata",
    "state": "West Bengal",
    "pincode": "700001"
  },
  "studentCapacity": 120,
  "coursesOffered": ["Java", "Data Analytics"],
  "contactEmail": "info@alphaskills.org",
  "contactPhone": "+919876543210"
}
201 Created → returns saved entity with generated id and server-side createdOn (epoch millis).

400 Bad Request → validation errors (see rules below).

409 Conflict → duplicate centerCode.

List Training Centers
GET /api/v1/training-centers
Returns an array of centers (200 OK).

Optional filters
GET /api/v1/training-centers?city=Kolkata&course=Java

Validation Rules (summary)
centerName: required, max 40 chars

centerCode: required, exactly 12 alphanumeric chars, unique

address: required (all fields)

pincode: 6-digit Indian style (e.g., 700001)

studentCapacity: ≥ 0

coursesOffered: array of non-blank strings

contactPhone: digits with optional +, 10–15 chars (e.g., +919876543210)

contactEmail: optional, but must be valid if present

createdOn: set by server on create (client input ignored)

Quick Testing
Postman
POST http://localhost:8080/api/v1/training-centers (valid) → 201

GET http://localhost:8080/api/v1/training-centers → 200

GET with ?city=&course= → 200

POST with invalid body → 400

POST with same centerCode again → 409

curl
bash

# Create (201)
curl -i -X POST http://localhost:8080/api/v1/training-centers \
  -H "Content-Type: application/json" \
  -d '{"centerName":"Alpha Skills","centerCode":"ABC123DEF456","address":{"detailedAddress":"12 Park Street","city":"Kolkata","state":"West Bengal","pincode":"700001"},"studentCapacity":120,"coursesOffered":["Java","Data Analytics"],"contactEmail":"info@alphaskills.org","contactPhone":"+919876543210"}'

# List (200)
curl -i http://localhost:8080/api/v1/training-centers
Build & Run JAR
bash
e
# from liv2train/
./mvnw clean package            # (or .\mvnw on Windows)
java -jar target/liv2train-0.0.1-SNAPSHOT.jar
Verify in MySQL (optional)
sql

USE liv2train;
SHOW TABLES;
SELECT * FROM training_centers ORDER BY id DESC LIMIT 5;
Troubleshooting
POST returns 404 in Postman
Ensure the URL is exactly /api/v1/training-centers with no trailing spaces/newlines (no %0A).

DB connection errors / startup fails
Check MySQL is running; verify application.properties credentials and database name liv2train.

Port 8080 in use
Add server.port=8081 to application.properties.

Validation not triggering
Set header Content-Type: application/json and ensure field names match the DTO.

Project Structure (key paths)
bash

liv2train/
├─ pom.xml
├─ mvnw / mvnw.cmd
├─ .mvn/wrapper/...
├─ src/
│  ├─ main/java/com/liv2train/liv2train/
│  │  ├─ Liv2trainApplication.java
│  │  ├─ controller/TrainingCenterController.java
│  │  ├─ service/TrainingCenterService.java
│  │  ├─ repository/TrainingCenterRepository.java
│  │  ├─ model/{TrainingCenter, Address}.java
│  │  ├─ dto/{TrainingCenterRequest, ErrorResponse}.java
│  │  └─ exception/{DuplicateResourceException, GlobalExceptionHandler}.java
│  └─ main/resources/
│     ├─ application-example.properties   (sample; optional)
│     └─ application.properties           (local credentials; not to be shared)
└─ target/ (build output)
