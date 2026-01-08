---

# ENG README (version English)

```md
[อ่าน README ภาษาไทย](README_TH.md)

# Flashcard Learning Platform

A web-based platform for creating, sharing, and learning Flashcards.  
Users can create their own Flashcards, browse public decks, and learn from Flashcards shared by other users.

## Problem Statement

Many learners struggle with memorization and lack a centralized platform for sharing learning materials.  
This project was developed to allow users to create, share, and learn Flashcards collaboratively.

## Features

- Login / Register / Logout
- Create, Edit, and Delete Flashcards
- Browse public Flashcards created by other users
- Save Flashcards to a personal library
- Share personal Flashcards with other users

## Tech Stack

Frontend

- React + TypeScript
- CSS

Backend

- Java Spring Boot

Database

- MongoDB

Testing

- JUnit

## System Architecture

The system is designed using a Client–Server architecture, clearly separating responsibilities into three main components: Frontend, Backend, and Database.

### Frontend (Presentation Layer)

The Frontend is responsible for rendering the user interface and sending requests to the Backend through REST APIs.  
It does not directly access the Database or handle business logic such as authentication or data processing.

### Backend (Application Layer)

The Backend receives requests from the Frontend, processes business logic, and communicates with the Database to manage data.

### Authentication

The system uses session-based authentication provided by Spring Boot (HttpSession).

- A session is created on the server after a successful login
- User information is stored on the server side
- A JSESSIONID cookie is sent back to the browser
- The browser automatically sends the cookie with subsequent requests
- Backend verifies the session to authorize API access

### Database (Data Layer)

MongoDB is used to store user and Flashcard data.  
The Database can only be accessed through the Backend.

- Supports full CRUD operations
- Uses a NoSQL document-based structure

## Authentication Flow

1. User submits username and password from the Frontend
2. Backend validates user credentials
3. Backend creates an HttpSession and stores user information
4. JSESSIONID cookie is sent back to the browser
5. Backend verifies the session for subsequent requests

## Port Configuration

- Frontend: http://localhost:5173
- Backend: http://localhost:8080

## Frontend Routes

### Authentication

- /login
- /register
- /logout

### Public Flashcard

- / => View all public Flashcards (home page)
- /deck_public/{deckId} => View details on the public Flashcard

### Private Flashcard

- /inventory => View all private Flashcard (inventory page)
- /deck_owner/{deckId} => View details on the private Flashcard
- /deck_finish => View the details of the private Flashcard that just learned
- /deck_create => View and add the details of the new Deck of Flashcard being created
- /deck_update/{deckId} => View and edit the details of Deck of Flashcard

## Backend API

Backend exposes RESTful API for frontend communication

## API Endpoints

### Authentication

- POST /flashcard/user/login => Used to login
- POST /flashcard/user/register => Used to register(create account)
- POST /flashcard/user/logout => Used to logout

### Public Flashcard

- GET /flashcard/deck/get_by_public => Used to view all Flashcards that have been made public
- GET /flashcard/card/get_by_deck_id/${deckId} => Used for viewing Cards on public Flashcards

### Private Flashcard

- GET /flashcard/deck/get_by_owner_deck_id => Used to view all Flashcards that have been made private
- GET /flashcard/deck/get_info/${deckId} => Used for viewing Cards on private Flashcards
- PUT /flashcard/card/update_progress_card => Used to update the status of each Card that has just been learned
- POST /flashcard/deck/create => Used for creating Decks of Flashcards
- POST /flashcard/deck/create_card => Used to create the details of each Card in a Flashcard
- GET /flashcard/card/get_by_deck_id/${deckId} => Used to view all Cards from each Deck of Flashcards
- PUT /flashcard/deck/update => Used to update the details for each Deck of Flashcards
- PUT /flashcard/card/update => Used to update the details for each Card in Deck of Flashcards
- DELETE /flashcard/deck/delete/${deckId} => Used to delete Deck of Flashcards

## Database Design

User

- id: ObjectID
- username: string
- password (plaintext): string

Deck (Deck of Flashcard)

- id: ObjectID
- ownerUserId: string
- deckName: string
- isPublic: Boolean
- tagList: Array
- createAt: Date
- updateAt: Date

Card

- id: ObjectID
- ownerDeckId: string
- frontcard: string
- backend: string
- state: string
- progress: int
- processingTime: Date
- createAt: Date
- updateAt: Date

## Security Considerations

- Passwords are currently stored as plaintext for learning purposes
- In a production environment, passwords should be hashed using bcrypt or similar algorithms
- JWT or Spring Security can be added to improve authentication security

## My Responsibility

This project was developed as a full-stack application.  
I was responsible for the entire development lifecycle, including system design, implementation, testing, and documentation.

### System & Backend

- Designed overall system architecture (Client–Server)
- Designed RESTful API and defined API endpoints
- Implemented backend logic and request handling
- Implemented session-based authentication using HttpSession
- Designed and implemented MongoDB data schema
- Connected backend with MongoDB and handled CRUD operations

### Frontend

- Designed UX/UI and application flow
- Developed frontend user interface
- Integrated frontend with backend APIs
- Handled client-side data rendering and user interaction

### Testing & Documentation

- Wrote unit tests for backend logic and API endpoints
- Tested API using tools such as Postman
- Created technical documentation and project README

## Challenges & What I Learned

This project was my first experience developing a full-stack web application.  
Throughout the development process, I faced several technical challenges and
gained a deeper understanding of modern web application architecture.

### Challenges

- Designing and developing a full-stack system with separated frontend and backend
- Designing REST APIs that correctly support frontend requirements
- Managing application state between frontend and backend
- Understanding request–response flow and session handling
- Structuring backend code into proper layers

### What I Learned

- Developed frontend using React with TypeScript
- Developed backend using Java Spring Boot
- Designed RESTful APIs and tested them using Postman
- Understood the communication flow between frontend and backend
- Learned backend layered architecture (Controller, Service, Repository, Entity)
- Gained hands-on experience with MongoDB integration
- Learned principles of unit testing using JUnit
- Understood system workflow and data flow within a web application

## How to Run

### Prerequisites

- Node.js (v18+ recommended)
- Java JDK 24
- Maven
- MongoDB (running locally or via Docker)

1. Clone Repository
   git clone https://github.com/chaau568/Flashcard.git
   cd Flashcard

2. Backend (Spring Boot)
   cd backend
   ./mvnw.cmd spring-boot:run
   => Backend runs on http://localhost:8080

3. Frontend (React + TypeScript)
   cd frontend
   npm install
   npm run dev
   => Frontend runs on http://localhost:5173
```
