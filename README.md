# 💪 StrongBeton
StrongBeton is a "workout diary" web application where each user can:
- Records your workouts and exercises
- Tracks your progress over time
- Compares results with other users
- ---
## ✨ Main functionalities
### 🔐 User Account Management
- ✅ Register and log into the system
- ✅ User data is protected and accessible only by the account owner
### 🏋️‍♀️ Workout Management
- ✅ Add new workouts after logging in
- ✅ View a list of previous workouts
### 🏆 Exercise Management within a Workout
- ✅ Add one or more exercises to each workout
- ✅ For each exercise, users can record:
  - Exercise name
  - Targeted muscle group
  - Repetitions and weights used
---
## 🖼️ Screenshots
### 🏆 Leaderboard
![Leaderboard](https://github.com/user-attachments/assets/6ef17382-eb6a-4458-906d-c8adfab424fe)
### 🔐 Register Form
![Register](https://github.com/user-attachments/assets/20d5cf92-4064-41e1-87cc-0c82332a2e01)
### 🏋️ Workout list
![WorkoutList](https://github.com/user-attachments/assets/795a304f-2791-430c-a15a-3a755422b222)
### 💪 Workout Details (CRUD)
![WorkoutDetails](https://github.com/user-attachments/assets/ce9142aa-b566-480c-a524-48c15387276b)
---
## 🛠️ Technologies Used
- ☕ Java 17
- 🌱 Spring Boot
- 🛡️ Spring Security
- 🔄 REST API
- 🗄️ MySQL + JPA/Hibernate
- 🎨 Angular
- 📦 Maven
---
## 🗃️ Database Structure
The following diagram shows the entity relationships in the database:
![DataBase](https://github.com/user-attachments/assets/6b125c5c-b10e-434b-b744-a89f4efbceda)
---
## 🚀 How to Run the Project Locally
Follow these steps to run the StrongBeton fitness diary application:
---
### 📂 1. Load and Execute the SQL Script
Ensure your MySQL server is running and execute the provided SQL script to create the strong_beton database and necessary tables.
---
### ⚙️ 2. Configure Spring Boot Application
In your application.properties file, make sure you have the following settings:
properties
spring.application.name=strongBeton
spring.datasource.url=jdbc:mysql://localhost:3306/strong_beton
spring.datasource.username=root
spring.datasource.password=root123
spring.main.banner-mode=off
server.port=8081
logging.level.org.springframework.security=DEBUG
# JWT settings
security.jwt.secret-key=39f27faa30598b17b1006fbdfa05578add57ea39ae7dec0b166c49f0cf0775c9
security.jwt.expiration-time=3600000  # 1 hour in milliseconds

```pom.xml
<dependencies>
  <dependency>spring-boot-starter-data-jpa</dependency>
  <dependency>modelmapper</dependency>
  <dependency>spring-boot-starter-validation</dependency>
  <dependency>spring-boot-starter-web</dependency>
  <dependency>spring-boot-devtools</dependency>
  <dependency>mysql-connector-j</dependency>
  <dependency>spring-boot-starter-security</dependency>
  <dependency>spring-boot-starter-test</dependency>
  <dependency>spring-security-test</dependency>

  <!-- JWT Dependencies -->
  <dependency>jjwt-api</dependency>
  <dependency>jjwt-impl</dependency>
  <dependency>jjwt-jackson</dependency>
</dependencies>
```
You can also run:
```bash
mvn clean install
```
Make sure it your Angular runs on port 4200:
```bash
cd frontend
npm install
ng serve --port 4200
```

## 🚀 How to Run the Project
```bash
git clone https://github.com/your-username/StrongBeton.git
cd StrongBeton
mvn clean install
java -jar target/strongbeton.jar
