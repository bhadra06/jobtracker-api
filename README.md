
# JobTracker AI — Backend

A production-grade REST API for job application tracking with AI-powered job description analysis, built with Java Spring Boot.

## Tech Stack

- Java 21 + Spring Boot 3.5
- Spring Security + JWT Authentication
- PostgreSQL + JPA/Hibernate
- Groq API (Llama 3.3) for AI features
- Maven
- Deployed on Railway

## Features

- JWT-based stateless authentication (register/login)
- Full CRUD for job applications with status tracking
- Role-based data isolation — users only see their own data
- AI JD Analyzer — matches job descriptions against user skills
- AI Career Chatbot — interview prep and career advice
- JD Summarizer — auto-summarizes job descriptions
- User profile management with skill tracking
- Dashboard stats endpoint (total, by-status breakdown)

## API Endpoints

### Auth — /api/auth
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | /register | Public | Register new user |
| POST | /login | Public | Login, get JWT token |

### User — /api/user
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | /profile | Auth | Get user profile + skills |
| PUT | /profile | Auth | Update name and skills |

### Applications — /api/applications
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | / | Auth | Create application |
| GET | / | Auth | Get all (newest first) |
| GET | /?status=APPLIED | Auth | Filter by status |
| GET | /:id | Auth | Get single application |
| PUT | /:id | Auth | Update application |
| DELETE | /:id | Auth | Delete application |
| GET | /stats | Auth | Dashboard stats |

### AI — /api/ai
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | /analyze | Auth | JD match analysis |
| POST | /chat | Auth | Career chatbot |
| POST | /summarize | Auth | Summarize JD |

## Application Status Flow

```
APPLIED → SHORTLISTED → INTERVIEW → OFFER
                                  → REJECTED
```

## Local Setup

### Prerequisites
- Java 21
- PostgreSQL
- Maven

### Steps

1. Clone the repository:
```bash
git clone https://github.com/bhadra06/jobtracker-api.git
cd jobtracker-api
```

2. Create PostgreSQL database:
```sql
CREATE DATABASE jobtracker_db;
```

3. Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jobtracker_db
spring.datasource.username=postgres
spring.datasource.password=your_password

app.jwt.secret=your_jwt_secret
app.jwt.expiration=86400000

groq.api.key=your_groq_api_key
groq.api.url=https://api.groq.com/openai/v1/chat/completions
```

4. Run the application:
```bash
mvn spring-boot:run
```

API runs at `http://localhost:8080`

## Environment Variables (Production)

| Variable | Description |
|----------|-------------|
| SPRING_DATASOURCE_URL | PostgreSQL connection string |
| SPRING_DATASOURCE_USERNAME | Database username |
| SPRING_DATASOURCE_PASSWORD | Database password |
| APP_JWT_SECRET | Secret key for JWT signing |
| APP_JWT_EXPIRATION | Token expiry in ms (86400000 = 24hrs) |
| GROQ_API_KEY | Groq API key for Llama 3.3 AI |
| GROQ_API_URL | https://api.groq.com/openai/v1/chat/completions |

## Important — Groq API Key

This project uses the **Groq API** (free tier) to power the AI features using the `llama-3.3-70b-versatile` model.

> ⚠️ The Groq API key configured in this project has an expiry date of **May 28, 2026**. After this date, a new API key must be generated at [console.groq.com](https://console.groq.com) and updated in the environment variables.

**Free tier limits:** 14,400 requests/day — sufficient for development and demo use.

## Database Schema

```
users
├── id (PK)
├── name
├── email (unique)
├── password (bcrypt hashed)
└── created_at

user_skills
├── user_id (FK → users)
└── skill

job_applications
├── id (PK)
├── company_name
├── role_name
├── job_description
├── job_url
├── status (APPLIED/SHORTLISTED/INTERVIEW/OFFER/REJECTED)
├── priority (LOW/MEDIUM/HIGH)
├── applied_date
├── follow_up_date
├── ai_match_score
├── notes
├── created_at
├── updated_at
└── user_id (FK → users)
```

## Author

**Chowtipalli Veera Bhadram**  
B.Tech Computer Science (Data Science) — KL University, May 2026  
AWS Cloud Practitioner | Red Hat Certified Enterprise App Developer

- GitHub: [github.com/bhadra06](https://github.com/bhadra06)
- LinkedIn: [linkedin.com/in/chowtipalli-veera-bhadram](https://linkedin.com/in/chowtipalli-veera-bhadram)
- Email: bhadrachowtipalli3@gmail.com