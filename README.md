Liv2Train — Backend Assignment (Spring Boot + MySQL)

Training Center registry API with create and list endpoints, validation, and duplicate handling.

Repo layout: the Spring Boot project is inside /liv2train.
When running commands below, cd into liv2train/ first.

Tech Stack

Java 17

Spring Boot 3.5.x (Web, Validation, Data JPA)

MySQL 8.0

Hibernate ORM (HikariCP)

Maven Wrapper (mvnw, mvnw.cmd)

(Optional) Postman for API testing

Requirements

Windows 10/11, macOS, or Linux

JDK 17+ (java -version should print 17.x)

MySQL 8 running on localhost:3306

Internet access on first run (Maven dependencies)

Getting the Code
Option A — Download ZIP

Click Code → Download ZIP on GitHub and extract.

Open the extracted folder and go into liv2train/ in your IDE or terminal.

Option B — Clone
git clone https://github.com/<your-username>/Backend_Liv2Train_SaaiJena.git
cd Backend_Liv2Train_SaaiJena/liv2train

Database Setup (MySQL)
CREATE DATABASE liv2train CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Optional: create a dedicated user for local dev
CREATE USER 'liv2train'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON liv2train.* TO 'liv2train'@'localhost';
FLUSH PRIVILEGES;

Application Configuration


Create liv2train/src/main/resources/application.properties (or copy from application-example.properties) and fill:

# --- MySQL datasource ---
spring.datasource.url=jdbc:mysql://localhost:3306/liv2train?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD

# --- JPA / Hibernate ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect  # optional for Boot 3.x

# (optional) Hikari tweaks
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=30000

Run the Application
IntelliJ IDEA

Open the liv2train/ folder as a project.

Wait for Maven to finish indexing and downloading dependencies.

Run Liv2trainApplication (green ▶).

You should see: Tomcat started on port(s): 8080.

Terminal (Maven Wrapper)

macOS / Linux:

./mvnw spring-boot:run


Windows (PowerShell/CMD):

.\mvnw spring-boot:run


Base URL: http://localhost:8080

API Endpoints
Create Training Center

POST /api/v1/training-centers

Request (JSON):

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


Responses

201 Created – saved entity with generated id and server-side createdOn (epoch millis).

400 Bad Request – validation errors (see rules below).

409 Conflict – duplicate centerCode.

List Training Centers

GET /api/v1/training-centers → 200 OK (array of centers)

Optional filters

GET /api/v1/training-centers?city=Kolkata&course=Java

Validation Rules (summary)

centerName: required, max 40 chars

centerCode: required, exactly 12 alphanumeric chars, unique

address: required (all fields), pincode 6 digits (e.g. 700001)

studentCapacity: ≥ 0

coursesOffered: array of non-blank strings

contactPhone: digits with optional +, 10–15 chars (e.g. +919876543210)

contactEmail: optional, must be valid if present

createdOn: server-generated on create (client value ignored)

Quick Testing
Postman

POST valid → 201

GET all → 200

GET with ?city=&course= → 200

POST invalid body → 400

POST same centerCode again → 409

curl

Create (201):

curl -i -X POST http://localhost:8080/api/v1/training-centers \
  -H "Content-Type: application/json" \
  -d '{"centerName":"Alpha Skills","centerCode":"ABC123DEF456","address":{"detailedAddress":"12 Park Street","city":"Kolkata","state":"West Bengal","pincode":"700001"},"studentCapacity":120,"coursesOffered":["Java","Data Analytics"],"contactEmail":"info@alphaskills.org","contactPhone":"+919876543210"}'


List (200):

curl -i http://localhost:8080/api/v1/training-centers

Build & Run JAR

Build:

./mvnw clean package


Run:

java -jar target/liv2train-0.0.1-SNAPSHOT.jar

Verify in MySQL (optional)
USE liv2train;
SHOW TABLES;
SELECT * FROM training_centers ORDER BY id DESC LIMIT 5;

Troubleshooting

POST returns 404 in Postman → Ensure the URL is exactly /api/v1/training-centers with no trailing spaces/newlines.

DB connection errors / startup fails → Check MySQL is running and credentials/DB name in application.properties.

Port 8080 in use → Add server.port=8081 in application.properties.

Validation not triggering → Set header Content-Type: application/json and ensure field names match the DTO.

Project Structure (key paths)
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
│     └─ application.properties           (local credentials; not shared)
└─ target/ (build output)
└─ target/ (build output)
