# 💪 StrongBeton

**A gamified strength-training platform that turns workout data into measurable, comparable performance metrics.**

StrongBeton is a full-stack web application (Angular · Spring Boot · MySQL) built as a Bachelor's thesis project at the Technical University of Varna. Unlike a plain workout log, every training set a user logs is run through an algorithmic scoring pipeline that accounts for body weight, rep ranges, and exercise difficulty turning raw numbers into a single strength score that can be tracked over time, compared between users, and pooled into clan-based team competition.

---

## ✨ Key Features

### 🔐 Authentication & Profiles
- Registration and login secured with **JWT** (stateless sessions) and **BCrypt** password hashing
- Role-based access control (regular users vs. `FEED_MODERATOR` / `OWNER`)
- Profile management with body-weight history tracking every weight change is stored as a new time-stamped record rather than overwritten, so it can feed into later strength calculations
- Friend system: send, accept, decline, and remove friend requests

### 🏋️ Workout & Exercise Tracking
- Create workouts, add exercises, and log sets (weight × reps) in a `DRAFT` state that becomes `FINISHED` once completed
- Exercises are categorized by muscle group, equipment type, and difficulty (compound / secondary / isolation)

### 📊 Algorithmic Performance Analysis
The core differentiator of the project — every logged set is processed through a dedicated scoring pipeline:
- **Effective weight** calculation for bodyweight exercises (e.g. pull-ups, dips, planks), using per-exercise coefficients combined with body weight, so bodyweight and external-weight exercises become comparable
- **Estimated one-rep max (1RM)**, calculated with a rep-range-aware approach that switches between different established strength-estimation formulas depending on how many reps were performed
- **Training volume** per set and per session
- **Relative (allometric) strength** — normalizes strength against body weight so users of different sizes can be compared fairly
- A combined **final strength score** per exercise, factoring in relative strength, volume, and an exercise-difficulty coefficient
- **Anomaly detection** on new results, using three independent checks (an unusually large percentage jump, an unusually fast day-over-day progress rate, and a statistical outlier check against the user's historical results) to flag suspicious or mistyped entries without blocking them
- **Progress forecasting** via linear regression on historical 1RM values, projecting a 30-day trend and classifying it as improving, declining, or stable

### 📢 Social Feed
- Personalized feed showing posts from the user and their friends (not a global feed)
- Like/unlike, comment, edit, and delete on own posts
- Deliberately **text- and results-focused rather than photo-focused** — a design decision aimed at keeping the social layer centered on real training progress instead of appearance-based comparison

### ⚔️ Clan System
- Create or join clans (open or approval-required), with `LEADER`, `MEMBER`, and `PENDING` roles
- Finished workouts automatically contribute points to the user's clan, subject to a daily contribution cap to prevent point-farming
- Clan leaderboard that accounts for member count and activity, not just raw totals

### 🛡️ Moderation Panel
- Restricted to `OWNER` / `FEED_MODERATOR` roles
- Search, hide, pin, delete posts/comments, and lock comment threads without hard-deleting content
- At-a-glance stats on total, visible, hidden, and pinned posts

---

## 🏗️ Architecture

StrongBeton follows a classic **N-tier architecture**:

| Layer | Responsibility | Technologies |
|---|---|---|
| **Presentation** | UI, user interaction, forms | Angular (standalone components), TypeScript, Tailwind CSS |
| **Application / Business Logic** | Request handling, auth, scoring algorithms, domain logic | Spring Boot, Spring Security, JWT |
| **Data Access** | Persistence, object-relational mapping | Spring Data JPA, Hibernate, MySQL |

On the backend, requests pass through a JWT auth filter → domain-organized REST controllers → service layer (including the algorithmic components: `StrengthScoreCalculator`, `AnomalyDetector`, `ProgressPredictor`) → JPA repositories.

On the frontend, the app is a single-page application with:
- Centralized routing config and two layout shells (`PublicLayout` for auth pages, `AppLayout` for the authenticated app)
- HTTP interceptors that transparently attach the API base URL and the JWT bearer token to every request
- Route guards (`authGuard`, `feedModeratorGuard`) controlling access at the client level
- A fully **responsive UI** with distinct desktop (sidebar navigation) and mobile (bottom navigation) layouts sharing the same services and business logic

---

## 🖼️ Screenshots

### 🏆 Leaderboard
![Leaderboard](https://github.com/user-attachments/assets/6ef17382-eb6a-4458-906d-c8adfab424fe)

### 🔐 Register Form
![Register](https://github.com/user-attachments/assets/20d5cf92-4064-41e1-87cc-0c82332a2e01)

### 🏋️ Workout List
![WorkoutList](https://github.com/user-attachments/assets/795a304f-2791-430c-a15a-3a755422b222)

### 💪 Workout Details (CRUD)
![WorkoutDetails](https://github.com/user-attachments/assets/ce9142aa-b566-480c-a524-48c15387276b)

---

## 🛠️ Technologies Used

**Backend**
- Java 17, Spring Boot
- Spring Security + JWT (HS256, via JJWT)
- BCrypt password hashing
- Spring Data JPA + Hibernate
- MySQL
- ModelMapper

**Frontend**
- Angular + TypeScript
- Tailwind CSS / SCSS
- Angular signals for session state management

**Infrastructure & Tooling**
- Cloudinary — external storage/delivery for profile images (keeps binary content out of the relational DB)
- Cloudflare — deployed in front of the live instance; used to analyze real-world traffic, encrypted request share, and probing/attack attempts during testing
- Figma — UI/UX prototyping
- Maven, npm

---

## 🗃️ Database Structure

The schema is organized around four main areas — **profile**, **workouts**, **social feed**, and **clans** — connected through the central `user` table. Key relations include one-to-many links from users to workouts and from workouts to exercise details and sets, a many-to-many user↔clan relationship through a membership table carrying role/points/join-date, and social tables for posts, comments, likes, and friendships.

Indexes are applied to the query patterns the app actually uses (e.g. workouts by user + status + date, weight-history by user + date, feed posts by creation date), alongside uniqueness constraints (username/email, one like per user per post, no duplicate clan membership) to keep the data consistent as the app scales.

The following diagram shows the entity relationships in the database:

![DataBase](https://github.com/user-attachments/assets/6b125c5c-b10e-434b-b744-a89f4efbceda)

---

## ✅ Testing

The system was validated through functional testing covering registration/login, workout creation and completion, and correctness of the strength-score calculation, followed by a real deployment monitored through Cloudflare to analyze live traffic patterns and validate the network-level security layer.

---

## 🔭 Possible Future Improvements

- Period-over-period progress comparison (weekly/monthly/yearly) and muscle-group load balance analysis
- Workout templates and "copy previous workout"
- Auto-generated workout-summary posts, seasonal clan leaderboards, and weekly challenges
- Refresh tokens, HttpOnly/Secure cookies instead of `localStorage`, and rate limiting
- Server-side caching and further client-side lazy loading
- PWA support with offline logging and sync
- OAuth login, wearable/health-platform integration (Google Fit / Apple Health), CSV/PDF export
- A coach module (already partially modeled in the DB) connecting trainees with coaches
- Admin-facing anomaly-score monitoring for leaderboard integrity

---

## 🚀 How to Run the Project Locally

### 📂 1. Set Up the Database
Ensure your MySQL server is running, then execute the provided SQL script to create the `strong_beton` database and its tables.

### ⚙️ 2. Configure the Backend
In `application.properties`, set the following:

```properties
spring.application.name=strongBeton
spring.datasource.url=jdbc:mysql://localhost:3306/strong_beton
spring.datasource.username=root
spring.datasource.password=root123
spring.main.banner-mode=off
server.port=8081
logging.level.org.springframework.security=DEBUG

# JWT settings
security.jwt.secret-key=your-secret-key-here
security.jwt.expiration-time=3600000  # 1 hour in milliseconds
```

> 💡 For a production or public deployment, move the JWT secret and DB credentials to environment variables instead of committing them in `application.properties`.

Key dependencies (`pom.xml`):

```xml
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

Build the backend:

```bash
mvn clean install
```

### 🖥️ 3. Run the Frontend
Make sure Angular runs on port 4200:

```bash
cd frontend
npm install
ng serve --port 4200
```

### ▶️ 4. Clone & Run End-to-End

```bash
git clone https://github.com/your-username/StrongBeton.git
cd StrongBeton
mvn clean install
java -jar target/strongbeton.jar
```
