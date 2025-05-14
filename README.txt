# Book and Author Management System

A comprehensive web application for managing books and authors using Spring Boot for the backend and JavaScript for the frontend.

## Features

- Book management (title, ISBN, category, year of publication, authors)
- Author management (name, nationality, date of birth, books)
- Many-to-many relationship between books and authors
- CRUD operations for both books and authors
- Responsive UI with search functionality
- API documentation with Swagger

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL Database
- Hibernate Validator
- Swagger/OpenAPI for API documentation

### Frontend
- HTML5
- CSS3
- JavaScript
- Bootstrap 5
- Axios for API requests

## ERD Diagram

```
+-------------+       +---------------+       +-------------+
|    BOOK     |       | BOOK_AUTHOR   |       |   AUTHOR    |
+-------------+       +---------------+       +-------------+
| id          |       | book_id       |       | id          |
| isbn        |       | author_id     |       | name        |
| title       |       +---------------+       | nationality |
| category    |                               | date_of_birth|
| publication_|                               +-------------+
| year        |                               
+-------------+                               
```

## Installation

### Prerequisites
- Java 17 or higher
- Maven
- MySQL

### Database Setup
1. Create a MySQL database (the application will create it if it doesn't exist):
```sql
CREATE DATABASE bookmanagement;
```

2. Update the database configuration in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookmanagement?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd Spring\ and\ JS\ Project
```

2. Build and run the backend:
```bash
mvn clean install
mvn spring-boot:run
```

3. The backend server will start on http://localhost:8080

4. Open the frontend:
   - Navigate to the `frontend` directory
   - Open `index.html` in a web browser, or
   - Use a local server like Live Server to serve the frontend

## API Documentation

The API documentation is available at:
- http://localhost:8080/swagger-ui.html

## API Endpoints

### Books

- `GET /books` - Get all books
- `GET /books/{id}` - Get book by ID
- `GET /books/isbn/{isbn}` - Get book by ISBN
- `GET /books/title/{title}` - Search books by title
- `GET /books/category/{category}` - Get books by category
- `POST /books` - Create a new book
- `PUT /books/{id}` - Update a book
- `DELETE /books/{id}` - Delete a book
- `POST /books/{id}/authors` - Add authors to a book
- `DELETE /books/{id}/authors` - Remove authors from a book

### Authors

- `GET /authors` - Get all authors
- `GET /authors/{id}` - Get author by ID
- `GET /authors/name/{name}` - Search authors by name
- `GET /authors/nationality/{nationality}` - Get authors by nationality
- `GET /authors/{id}/books` - Get author with their books
- `POST /authors` - Create a new author
- `PUT /authors/{id}` - Update an author
- `DELETE /authors/{id}` - Delete an author
- `POST /authors/{id}/books` - Add books to an author
- `DELETE /authors/{id}/books` - Remove books from an author

## Usage

1. **Home Screen**: From the home screen, you can navigate to Books or Authors section.

2. **Books Management**:
   - View all books in a table format
   - Search for books by title, ISBN, or category
   - Add a new book with the "Add New Book" button
   - Edit or delete books using the action buttons
   - View detailed information about a book

3. **Authors Management**:
   - View all authors in a table format
   - Search for authors by name or nationality
   - Add a new author with the "Add New Author" button
   - Edit or delete authors using the action buttons
   - View detailed information about an author and their books
