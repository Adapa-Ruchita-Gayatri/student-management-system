# Student Management System

## Overview
This project is a Student Management System built with Spring Boot. It provides a RESTful API for managing student records, including operations like adding, retrieving, updating, and deleting student information.

## Features
- Add new students
- Retrieve student information by ID or name
- Update student information
- Delete student records
- Paginated retrieval of all students with optional name filtering

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL Database
- Redis (for caching)
- Gradle (for build management)
- Lombok (for reducing boilerplate code)
- Springdoc OpenAPI (for API documentation)
- Docker (for running MYSQL, Redis) locally
- ChatGPT (for JavaDocs generation)

## Setup and Installation
1. Ensure you have Java 17 and Docker installed on your systed
2. Clone the repository
3. Run docker-compose up -d


## API Endpoints

- **POST** `/api/students` - Add a new student
- **GET** `/api/students/{id}` - Get a student by ID
- **GET** `/api/students` - Get students by name (query parameter)
- **PUT** `/api/students/{id}` - Update a student's information
- **DELETE** `/api/students/{id}` - Delete a student
- **GET** `/api/students/all` - Get all students (with pagination and optional name filter)

## Configuration

- The application runs on **port 8080** by default.
- Redis TTL (Time-To-Live) is set to **300 seconds**.