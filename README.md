# TFG App - Personal Wellness Assistant

A comprehensive full-stack application designed to help users track their habits, manage their daily moods, and receive personalized wellness guidance through an AI-powered assistant.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

TFG App is a wellness application that combines habit tracking, mood management, and AI-powered personalized recommendations. Users can create and track daily habits, record their emotional states, earn achievements through trophies, and interact with an intelligent assistant that provides tailored wellness advice based on their activity and mood patterns.

## ✨ Features

- **Habit Tracking**: Create, manage, and track daily habits with streak monitoring
- **Mood Journaling**: Record daily moods and connect them with diary entries
- **Weekly Summaries**: Automatic generation of weekly wellness summaries with statistics
- **Trophy System**: Achievement-based rewards for maintaining habit streaks
- **AI Assistant**: Personalized wellness recommendations powered by LangChain4j and OpenAI
- **Multi-language Support**: Available in Spanish (ES), English (EN), and Galician (GL)
- **User Authentication**: Secure JWT-based authentication system
- **Responsive Design**: Mobile-first React Native frontend with NativeWind styling

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Database**: H2
- **Authentication**: JWT (JSON Web Tokens)
- **AI Integration**: LangChain4j with OpenAI API
- **ORM**: JPA/Hibernate
- **Testing**: JUnit, Spring Test

### Frontend
- **Framework**: React Native (Expo)
- **Language**: JavaScript
- **State Management**: Zustand (Beta)
- **HTTP Client**: Axios
- **Styling**: NativeWind (Tailwind CSS for React Native)
- **Routing**: Expo Router
- **Internationalization**: i18next
- **Storage**: AsyncStorage

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Build Tools**: Maven (Backend), npm (Frontend)

## 📁 Project Structure

```
tfg-app/
├── backend/
│   ├── src/
│   │   ├── main/java/com/tfg/tfg_app/
│   │   │   ├── model/
│   │   │   │   ├── entities/          # JPA entities
│   │   │   │   ├── services/          # Business logic
│   │   │   │   └── common/            # Shared utilities
│   │   │   ├── rest/
│   │   │   │   ├── controllers/       # REST endpoints
│   │   │   │   ├── dtos/              # Data transfer objects
│   │   │   │   └── common/            # JWT and auth
│   │   │   └── Application.java
│   │   └── test/                      # Unit and integration tests
│   ├── pom.xml
│   ├── Dockerfile
│   └── HELP.md
├── frontend/
│   ├── src/
│   │   ├── app/                       # Navigation structure
│   │   ├── components/                # Reusable components
│   │   ├── services/                  # API client services
│   │   ├── context/                   # React context (Auth)
│   │   ├── i18n/                      # Internationalization config
│   │   └── store/                     # Zustand state management
│   ├── package.json
│   ├── metro.config.js
│   └── tailwind.config.js
├── compose.yml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Node.js** 16+ and npm
- **Docker** and **Docker Compose** (optional)
- **OpenAI API Key** (for AI assistant features)

### Backend Setup

1. **Navigate to the backend directory**:
   ```bash
   cd backend
   ```

2. **Configure environment variables** in `.env`:
   ```env
   OPENAI_API_KEY=your_openai_api_key
   OPENAI_MODEL=gpt-3.5-turbo
   ```

3. **Build the project**:
   ```bash
   mvn clean package
   ```

4. **Run tests**:
   ```bash
   mvn test
   ```

### Frontend Setup

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Configure the API client** in `frontend/src/services/apiClient.js`:
   ```javascript
   const getBaseURL = () => {
       if (__DEV__ || Platform.OS === 'android') {
           return 'http://192.168.18.4:8080/api';  // Change to your backend URL
       } else {
           return 'http://localhost:8080/api';
       }
   }
   ```

## ▶️ Running the Application

### Option 1: Using Docker Compose (In progress)

1. **Start all services**:
   ```bash
   docker-compose up -d
   ```

   This starts:
   - Spring Boot backend on port 8080
   - H2 in-memory database with automatic initialization

2. **Access the application**:
   - Backend API: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`

### Option 2: Local Development

**Start the Backend**:
```bash
cd backend
mvn spring-boot:run
```

**Start the Frontend**:
```bash
cd frontend

npx expo start
```

Then scan the QR code with your phone or use an emulator.

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/users/signup` - Register new user
- `POST /api/users/login` - Login user
- `POST /api/users/login-service-token` - Login with service token

### Habits
- `GET /api/habits` - Get all available habits
- `GET /api/habits/user` - Get user's habits
- `POST /api/habits/user/{habitId}` - Add habit to user
- `DELETE /api/habits/user/{userHabitId}` - Remove habit from user
- `POST /api/habits/entries` - Create habit entry
- `DELETE /api/habits/entries/{habitEntryId}` - Delete habit entry

### Moods & Diary
- `GET /api/moods` - Get all moods
- `GET /api/diary` - Get user's diary entries
- `POST /api/diary` - Create diary entry
- `DELETE /api/diary/{diaryEntryId}` - Delete diary entry

### Trophies
- `GET /api/trophies` - Get all trophies
- `GET /api/trophies/user-trophy` - Get user's trophies
- `GET /api/trophies/user-trophy/habit?habitId={habitId}` - Get trophies for specific habit

### Weekly Summaries
- `GET /api/weekly-summaries` - Get user's weekly summaries
- `GET /api/weekly-summaries/{summaryId}` - Get specific summary

### AI Assistant
- `POST /api/assistant/chat` - Send message to assistant

For detailed API specifications, see the controller classes in `backend/src/main/java/com/tfg/tfg_app/rest/controllers`.

## 🧪 Testing

### Run Backend Tests
```bash
cd backend
mvn test
```

Test coverage includes:
- Service layer tests
- Trophy system tests
- Assistant service tests
- Weekly summary tests

## 🐳 Deployment

### Docker Build

**Backend**:
```bash
cd backend
docker build -t tfg-app-backend .
```

**Using Docker Compose**:
```bash
docker-compose up -d
```

### Environment Configuration

Create a `.env` file in the project root with necessary environment variables for your deployment environment.

## 📝 Key Features Explained

### Habit Tracking
Users can select from predefined habits or create custom ones. The system tracks:
- Completion status
- Streak count (consecutive days)
- Historical data

### Trophy System
Achievements are awarded based on habit streak milestones:
- 7-day streak: Week Warrior
- 30-day streak: Monthly Master
- Multi-language trophy names and descriptions

### Weekly Summaries
Automatic summaries generated weekly containing:
- Habits completed
- Total diary entries
- Trophies earned
- Biggest streak
- Mood trend

### AI Assistant
Powered by LangChain4j with OpenAI, the assistant:
- Maintains conversation memory
- Provides personalized advice based on user habits and moods
- Responds in user's preferred language
- Offers practical wellness recommendations

## 🌍 Internationalization

The app supports three languages:
- **ES** - Spanish
- **EN** - English
- **GL** - Galician

Language preferences are stored in AsyncStorage and can be changed in the profile section.

## 📧 Contact & Support

For issues, feature requests, or contributions, please open an issue in the repository.