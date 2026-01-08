---

# TH README (เวอร์ชันภาษาไทย)

```md
# Flashcard Learning Platform

แพลตฟอร์มสำหรับสร้างและแชร์ Flashcards  
รองรับการ Login, แชร์ Deck, และเล่น Flashcard ของผู้อื่น

## Problem Statement

ผู้เรียนจำนวนมากประสบปัญหาในการจดจำเนื้อหา และไม่มีแพลตฟอร์มกลางสำหรับแชร์ Flashcards  
โปรเจกต์นี้ถูกพัฒนาขึ้นเพื่อให้ผู้ใช้สามารถสร้าง แชร์ และเรียนรู้ Flashcards ร่วมกันได้

## Features

- Login / Register / Logout
- สร้าง แก้ไข และลบ Flashcards
- ดู Flashcards สาธารณะจากผู้ใช้คนอื่น
- บันทึก Flashcards ลงคลังส่วนตัว
- แชร์ Flashcards ของตนเองให้ผู้อื่น

## Tech Stack

**Frontend**

- React + TypeScript
- CSS

**Backend**

- Java Spring Boot

**Database**

- MongoDB

**Testing**

- JUnit

## System Architecture

ระบบถูกออกแบบในลักษณะ Client–Server Architecture  
โดยแยก Frontend, Backend และ Database ออกจากกันอย่างชัดเจน

### Frontend (Presentation Layer)

Frontend ทำหน้าที่แสดงผล UI และส่ง request ไปยัง Backend ผ่าน REST API ไม่ติดต่อกับ Database โดยตรง และไม่จัดการ business logic

- ส่ง HTTP request ไปยัง Backend
- รับ response ในรูปแบบ JSON
- Browser จะจัดการ cookie (JSESSIONID) โดยอัตโนมัติ

### Backend (Application Layer)

Backend ทำหน้าที่รับ request ประมวลผล logic และจัดการข้อมูลผ่าน Database

### Authentication

ระบบใช้ Session-based Authentication ด้วย HttpSession ของ Spring Boot  
หลังจาก login สำเร็จ ระบบจะสร้าง session และใช้ cookie (JSESSIONID) ในการยืนยันตัวตน

- เมื่อผู้ใช้ login สำเร็จ ระบบจะสร้าง session และเก็บข้อมูล user ไว้ฝั่ง server
- Backend จะส่ง JSESSIONID cookie กลับไปยัง browser
- ทุก request หลังจากนั้น browser จะส่ง cookie นี้กลับมาโดยอัตโนมัติ
- Backend ใช้ session เพื่อตรวจสอบสิทธิ์การเข้าถึง API

ระบบยังไม่ได้ใช้ JWT หรือ token-based authentication  
แต่โครงสร้างถูกออกแบบให้สามารถพัฒนาไปใช้ JWT หรือ Spring Security ได้ในอนาคต

### Database (Data Layer)

ใช้ MongoDB ในการจัดเก็บข้อมูลผู้ใช้และ Flashcards  
Database สามารถเข้าถึงได้เฉพาะผ่าน Backend เท่านั้น

- รองรับการทำ CRUD operations
- ใช้ NoSQL document structure

## Authentication Flow

1. ผู้ใช้ส่ง username และ password จาก Frontend
2. Backend ตรวจสอบข้อมูลผู้ใช้
3. Backend สร้าง HttpSession และเก็บ userId
4. ส่ง JSESSIONID cookie กลับไปยัง browser
5. Request ถัดไป Backend ตรวจสอบ session เพื่อยืนยันตัวตน

## Port Configuration

- Frontend: http://localhost:5173
- Backend: http://localhost:8080

## Frontend Routes

### Authentication

- /login
- /register
- /logout

### Public Flashcard

- / => ดูแฟลชการ์ดทั้งหมดที่เป็นของสาธารณะ (home page)
- /deck_public/{deckId} => ดูรายละเอียดของแฟลชการ์ดที่เป็นของสาธารณะ

### Private Flashcard

- /inventory => ดูแฟลชการ์ดทั้งหมดที่เป็นของส่วนตัว (inventory page)
- /deck_owner/{deckId} => ดูรายละเอียดของแฟลชการ์ดที่เป็นของส่วนตัว
- /deck_finish => ดูรายละเอียดของแฟลชการ์ดที่พึ่งเรียนเสร็จไป ว่ามีสถานะอะไรบ้าง
- /deck_create => ดูและเพิ่มรายละเอียดของชุดแฟลชการ์ดใหม่ที่กำลังสร้าง
- /deck_update/{deckId} => ดูและแก้ไขรายละเอียดของชุดแฟลชการ์ด

## Backend API

Backend exposes RESTful API for frontend communication

## API Endpoints

### Authentication

- POST /flashcard/user/login => ใช้เพื่อเข้าสู่ระบบ
- POST /flashcard/user/register => ใช้เพื่อลงทะเบียน(สร้างบัญชี)
- POST /flashcard/user/logout => ใช้เพื่อออกจากระบบ

### Public Flashcard

- GET /flashcard/deck/get_by_public => ใช้เพื่อดูแฟลชการ์ดทั้งหมดที่เผยแพร่สู่สาธารณะแล้ว
- GET /flashcard/card/get_by_deck_id/${deckId} => ใช้เพื่อดูบัตรคำศัพท์บนแฟลชการ์ดสาธารณะ

### Private Flashcard

- GET /flashcard/deck/get_by_owner_deck_id => ใช้สำหรับดูแฟลชการ์ดทั้งหมดที่ถูกตั้งค่าเป็นส่วนตัว
- GET /flashcard/deck/get_info/${deckId} => ใช้สำหรับดูการ์ดในแฟลชการ์ดส่วนตัว
- PUT /flashcard/card/update_progress_card => ใช้สำหรับอัปเดตสถานะของการ์ดแต่ละใบที่เพิ่งเรียนรู้ไป
- POST /flashcard/deck/create => ใช้สำหรับสร้างชุดการ์ด
- POST /flashcard/deck/create_card => ใช้สำหรับสร้างรายละเอียดของการ์ดแต่ละใบในแฟลชการ์ด
- GET /flashcard/card/get_by_deck_id/${deckId} => ใช้สำหรับดูการ์ดทั้งหมดจากแต่ละสำรับของแฟลชการ์ด
- PUT /flashcard/deck/update => ใช้สำหรับอัปเดตรายละเอียดของแต่ละชุดแฟลชการ์ด
- PUT /flashcard/card/update => ใช้สำหรับอัปเดตรายละเอียดของแต่ละการ์ดในชุดแฟลชการ์ด
- DELETE /flashcard/deck/delete/${deckId} => ใช้สำหรับลบชุดแฟลชการ์ด

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

- ปัจจุบันรหัสผ่านถูกจัดเก็บในรูปแบบข้อความธรรมดาเพื่อวัตถุประสงค์ในการเรียนรู้
- ในสภาพแวดล้อมการใช้งานจริง ควรเข้ารหัสรหัสผ่านโดยใช้ bcrypt หรืออัลกอริทึมที่คล้ายกัน
- สามารถเพิ่ม JWT หรือ Spring Security เพื่อปรับปรุงความปลอดภัยในการตรวจสอบสิทธิ์ได้

## My Responsibility

ผมรับผิดชอบการพัฒนาโปรเจกต์นี้แบบ Full-stack  
ตั้งแต่การออกแบบระบบ พัฒนา ทดสอบ และจัดทำเอกสาร

### System & Backend

- ออกแบบสถาปัตยกรรมระบบโดยรวม (Client-Server)
- ออกแบบ RESTful API และกำหนดเอนด์พอยต์ของ API
- พัฒนาตรรกะ Backend และการจัดการคำขอ
- พัฒนาการตรวจสอบสิทธิ์แบบเซสชันโดยใช้ HttpSession
- ออกแบบและพัฒนาสคีมาข้อมูล MongoDB
- เชื่อมต่อแบ็กเอนด์กับ MongoDB และจัดการการดำเนินการ CRUD

### Frontend

- ออกแบบ UX/UI และขั้นตอนการใช้งานแอปพลิเคชัน
- พัฒนาส่วนติดต่อผู้ใช้ (UI) บนฝั่งไคลเอนต์
- ผสานรวมส่วนติดต่อผู้ใช้กับ API บนฝั่งเซิร์ฟเวอร์ (API)
- จัดการการแสดงผลข้อมูลฝั่งไคลเอ็นต์และการโต้ตอบกับผู้ใช้

### Testing & Documentation

- เขียน Unit Test สำหรับตรรกะฝั่ง Backend และ API Endpoint
- ทดสอบ API โดยใช้เครื่องมือต่างๆ เช่น Postman
- จัดทำเอกสารทางเทคนิคและ README ของโครงการ

## Challenges & What I Learned

โปรเจกต์นี้เป็นประสบการณ์แรกในการพัฒนา Full-stack  
ทำให้เข้าใจโครงสร้างระบบ การสื่อสารระหว่าง Frontend–Backend  
และการไหลของข้อมูลในระบบ Web Application อย่างลึกซึ้ง

### Challenges

- ออกแบบและพัฒนาระบบฟูลสแต็กที่มีการแยกส่วน Frontend และ Backend
- ออกแบบ REST API ที่รองรับความต้องการของฟรอนต์เอนด์ได้อย่างถูกต้อง
- บริหารจัดการสถานะของแอปพลิเคชันระหว่าง Frontend และ Backend
- เข้าใจกระบวนการไหลของคำขอ-การตอบสนองและการจัดการเซสชัน
- จัดโครงสร้างโค้ดแบ็กเอนด์ให้เป็นเลเยอร์ที่เหมาะสม

### What I Learned

- พัฒนาส่วนหน้า (frontend) โดยใช้ React ร่วมกับ TypeScript
- พัฒนาส่วนหลัง (backend) โดยใช้ Java Spring Boot
- ออกแบบและทดสอบ RESTful API โดยใช้ Postman
- เข้าใจกระบวนการสื่อสารระหว่างส่วนหน้าและส่วนหลัง
- เรียนรู้สถาปัตยกรรมแบบเลเยอร์ของส่วนหลัง (Controller, Service, Repository, Entity)
- ได้รับประสบการณ์จริงในการผสานรวม MongoDB
- เรียนรู้หลักการของการทดสอบหน่วย (unit testing) โดยใช้ JUnit
- เข้าใจเวิร์กโฟลว์ของระบบและการไหลของข้อมูลภายในเว็บแอปพลิเคชัน

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
   => Backend run on http://localhost:8080

3. Frontend (React + TypeScript)
   cd frontend
   npm install
   npm run dev
   => Frontend run on http://localhost:5173
```
