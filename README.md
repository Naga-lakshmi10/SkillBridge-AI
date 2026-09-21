\# 🚀 SkillBridge AI



\### Bridge the gap. Build the career.



\*\*SkillBridge AI\*\* is an AI-powered career guidance platform that helps students understand how well their current profile matches a target job role and what they should do next to become job-ready.



Instead of providing only a resume score, SkillBridge AI connects \*\*resume analysis, job requirements, skill gaps, resume evidence gaps, project recommendations, career planning, and re-analysis\*\* into one workflow.



\---



\## 🎯 Problem



Students often struggle to understand whether their current skills and resume actually match the requirements of the jobs they want.



A job description may contain technologies such as Java, Spring Boot, REST APIs, SQL, Git, and Data Structures \& Algorithms, but students may not know:



\* Which required skills they already have

\* Which skills are missing

\* What evidence is missing from their resume

\* Which projects they should build

\* What they should learn next

\* How to improve their resume for the target role



Existing career tools often focus on only one area, such as resume checking, job searching, or interview preparation.



\*\*SkillBridge AI brings these activities together into one actionable workflow.\*\*



\---



\## 💡 Solution



SkillBridge AI allows a student to:



1\. Upload their resume as a PDF

2\. Enter a target job role

3\. Provide the job description or required skills

4\. Analyze their profile using AI

5\. View matched and missing skills

6\. Identify gaps in their resume

7\. Get personalized recommendations

8\. Discover suitable projects to build

9\. Receive resume improvement suggestions

10\. Follow a personalized four-phase career roadmap

11\. Change the target requirements and re-analyze their profile



\### Core Workflow



```text

Resume + Target Job

&#x20;       ↓

&#x20;  Resume Extraction

&#x20;       ↓

&#x20;   AI Analysis

&#x20;       ↓

&#x20;┌───────────────────────┐

&#x20;│ Career Match          │

&#x20;│ Matched Skills        │

&#x20;│ Missing Skills        │

&#x20;│ Resume Gaps           │

&#x20;│ Recommendations       │

&#x20;│ Project Suggestions   │

&#x20;│ Resume Improvements   │

&#x20;│ Career Roadmap        │

&#x20;└───────────────────────┘

&#x20;       ↓

&#x20;  Improve Profile

&#x20;       ↓

&#x20;    Re-analyze

```



\---



\## ✨ Key Features



\### 📄 Resume Analysis



Upload a PDF resume and extract its text automatically.



\### 🎯 Target Role Analysis



Provide a target role such as:



\* Java Backend Developer

\* Software Engineer

\* Full Stack Developer

\* Other technical roles



\### 📊 Career Match



Get an AI-generated match percentage based on the relationship between the resume profile and target job requirements.



\### ✅ Matched Skills



Identify skills from the resume that align with the target requirements.



\### ❌ Missing Skills



Identify important skills that are required by the target role but are not sufficiently represented in the profile.



\### 🔎 Resume Gap Analysis



Go beyond missing skills by identifying areas where the student may need stronger evidence in their resume.



For example:



> A student may know Java but may need a practical Spring Boot or REST API project to demonstrate backend development experience.



\### 🛠️ Project Recommendations



The platform recommends practical projects that can help students build evidence for their missing skills.



\### 📝 Resume Improvements



Provides suggestions for improving resume content, including how to better present skills, projects, and achievements.



\### 🗺️ Personalized Career Roadmap



Generates a four-phase roadmap covering areas such as:



1\. Skill gap closure

2\. Project development

3\. Interview preparation

4\. Resume and portfolio strengthening



\### 🔄 Re-analysis



Students can change their target requirements and analyze their profile again to understand how their career fit changes.



\---



\## 🌟 What Makes SkillBridge AI Different?



The main idea behind SkillBridge AI is its \*\*closed-loop career guidance workflow\*\*.



A traditional resume checker may answer:



> "How well does my resume match this job?"



SkillBridge AI aims to answer:



> "What is missing, how can I improve it, what should I build, and what should I do next?"



\### Closed-Loop Workflow



```text

Resume

&#x20; ↓

Job Requirements

&#x20; ↓

AI Analysis

&#x20; ↓

Skill Gaps

&#x20; ↓

Resume Gaps

&#x20; ↓

Recommended Projects

&#x20; ↓

Personalized Roadmap

&#x20; ↓

Resume Improvements

&#x20; ↓

Re-analysis

```



This transforms a static resume into an \*\*actionable career improvement plan\*\*.



\---



\## 🏗️ System Architecture



```text

&#x20;                   ┌──────────────────┐

&#x20;                   │     Student      │

&#x20;                   └────────┬─────────┘

&#x20;                            │

&#x20;                            ▼

&#x20;                ┌──────────────────────┐

&#x20;                │ React + Vite Frontend│

&#x20;                └──────────┬───────────┘

&#x20;                           │ REST API

&#x20;                           ▼

&#x20;                ┌──────────────────────┐

&#x20;                │  Spring Boot Backend │

&#x20;                └───────┬───────┬──────┘

&#x20;                        │       │

&#x20;             ┌──────────┘       └──────────┐

&#x20;             ▼                             ▼

&#x20;      ┌──────────────┐              ┌──────────────┐

&#x20;      │   PDFBox     │              │ Gemini API   │

&#x20;      │ Resume Parser│              │ AI Analysis  │

&#x20;      └──────────────┘              └──────────────┘

&#x20;                        │

&#x20;                        ▼

&#x20;                 ┌──────────────┐

&#x20;                 │    MySQL     │

&#x20;                 │   Database   │

&#x20;                 └──────────────┘

```



\---



\## 🛠️ Technology Stack



\### Frontend



\* React

\* Vite

\* JavaScript

\* HTML

\* CSS

\* React Router



\### Backend



\* Java 21

\* Spring Boot

\* Spring REST APIs

\* Maven

\* Hibernate

\* JPA



\### AI



\* Google Gemini API

\* Gemini 3.6 Flash



\### Resume Processing



\* Apache PDFBox



\### Database



\* MySQL



\### Development \& Deployment



\* Git

\* GitHub

\* Railway

\* IntelliJ IDEA

\* Visual Studio Code



\---



\## 📁 Project Structure



```text

SkillBridge-AI/

│

├── frontend/

│   ├── src/

│   │   ├── pages/

│   │   ├── components/

│   │   ├── App.jsx

│   │   └── index.css

│   ├── package.json

│   └── vite.config.js

│

├── skillbridge-ai/

│   ├── src/

│   │   └── main/

│   │       ├── java/

│   │       │   └── com/

│   │       │       └── skillbridge/

│   │       │           └── skillbridgeai/

│   │       │               ├── controller/

│   │       │               ├── service/

│   │       │               ├── model/

│   │       │               └── config/

│   │       └── resources/

│   │

│   ├── pom.xml

│   └── application.properties

│

└── README.md

```



\---



\## 🔌 Backend API



\### Health Check



```http

GET /api/health

```



Checks whether the backend service is running.



\### Resume PDF Extraction



```http

POST /api/pdf/extract

```



Accepts a PDF resume and extracts its text using Apache PDFBox.



\### Career Analysis



```http

POST /api/analysis

```



Accepts the student's resume information and target job requirements and returns the career analysis.



\---



\## 🚀 Running Locally



\### Prerequisites



Make sure the following are installed:



\* Java 21+

\* Maven

\* Node.js

\* npm

\* MySQL

\* Git



You also need a Gemini API key for AI-powered analysis.



\---



\### 1. Clone the Repository



```bash

git clone https://github.com/Naga-lakshmi10/SkillBridge-AI.git

cd SkillBridge-AI

```



\---



\### 2. Configure MySQL



Create the database:



```sql

CREATE DATABASE skillbridge\_ai;

```



Configure the database connection in the Spring Boot application.



\---



\### 3. Configure Gemini API



Set your Gemini API key as an environment variable:



```text

GEMINI\_API\_KEY=your\_api\_key

```



\*\*Never commit your API key to GitHub.\*\*



\---



\### 4. Run the Backend



```bash

cd skillbridge-ai

mvn spring-boot:run

```



The backend will run on:



```text

http://localhost:8080

```



\---



\### 5. Run the Frontend



Open another terminal:



```bash

cd frontend

npm install

npm run dev

```



The frontend will be available at:



```text

http://localhost:5173

```



\---



\## 🌐 Live Demo



\*\*Live Application:\*\*



https://worthy-radiance-production-eb6f.up.railway.app



\*\*GitHub Repository:\*\*



https://github.com/Naga-lakshmi10/SkillBridge-AI



\---



\## 🧪 Example Use Case



Suppose a student wants to become a:



\*\*Java Backend Developer\*\*



They provide a job description containing:



```text

Java

Spring Boot

REST APIs

MySQL

SQL

Git

OOP

Data Structures and Algorithms

```



The student uploads their resume.



SkillBridge AI analyzes both inputs and can identify:



```text

Career Match: 60%



Matched Skills:

✓ Java

✓ MySQL

✓ Git

✓ OOP



Missing Skills:

✗ Spring Boot

✗ REST APIs

```



The system can then recommend:



\* Skills to learn

\* Projects to build

\* Resume improvements

\* Interview preparation

\* A structured career roadmap



The student can later modify the target requirements and \*\*re-analyze the profile\*\*.



\---



\## 🔐 Security



Sensitive configuration values such as API keys and database credentials are stored through environment variables.



The project does not commit secrets to the GitHub repository.



\---



\## 🔮 Future Scope



Potential future improvements include:



\* GitHub profile analysis

\* Job portal integration

\* Automated job matching

\* Interview question generation

\* AI mock interviews

\* Resume builder

\* Progress tracking

\* Authentication and user profiles

\* Skill progress history

\* More detailed project-to-skill mapping

\* Personalized learning resource recommendations



\---



\## 🎯 Hackathon



Built for:



\*\*HACK DEVENGERS 2.0\*\*



\### Project



\*\*SkillBridge AI\*\*



\### Tagline



> \*\*Bridge the gap. Build the career.\*\*



\---



\## 👩‍💻 Author



\*\*Nagalakshmi Karri\*\*



Computer Science \& Engineering Student



GitHub:

https://github.com/Naga-lakshmi10



\---



\## 📜 License



This project is developed as a hackathon project and for educational purposes.



