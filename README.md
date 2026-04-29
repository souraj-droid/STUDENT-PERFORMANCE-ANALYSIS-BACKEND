# Student Performance Analytics and Reporting System

A comprehensive **backend-only** Spring Boot REST API for analyzing student performance data and generating reports with actionable insights for educators and students.

## Features

### Admin (Teacher) Operations
- **Student Management**: Create, read, update, and delete student records
- **Performance Data Input**: Record and manage student grades and exam results
- **Analytics Dashboard**: Comprehensive performance statistics and insights
- **Department-Level Reporting**: Track performance by academic departments
- **At-Risk Student Identification**: Identify students who need additional support
- **Performance Trend Analysis**: Monitor academic progress over time

### Student Operations
- **Personal Performance Viewing**: Access individual academic records
- **Academic Progress Tracking**: Monitor semester-by-semester performance
- **GPA Calculation**: Automatic grade point average computation
- **Academic Standing**: Determine current academic status
- **Personalized Recommendations**: Get improvement suggestions based on performance

### System Capabilities
- **Role-Based Authentication**: Secure access control (ADMIN/STUDENT roles)
- **RESTful API**: 20+ endpoints for comprehensive data management
- **Advanced Analytics**: Intelligent performance analysis and reporting
- **MySQL Database**: Robust data persistence with JPA/Hibernate
- **Automatic Grade Calculation**: Built-in grade computation logic
- **Sample Data**: Pre-populated with realistic test data

## Technology Stack

- **Spring Boot 3.2.5** - Main application framework
- **Spring Web** - REST API development
- **Spring Data JPA** - Database access layer
- **Spring Security** - Authentication and authorization
- **MySQL** - Database management
- **Thymeleaf** - Template engine (for future UI development)
- **Lombok** - Code generation and boilerplate reduction
- **Maven** - Build and dependency management

## Project Structure

```
StudentPerformanceAnalytics/
|
|-- src/main/java/com/student/performance/
|   |-- StudentPerformanceApplication.java     # Main application class
|   |-- config/
|   |   |-- SecurityConfig.java               # Security configuration
|   |   |-- DataInitializer.java              # Sample data setup
|   |-- controller/
|   |   |-- AdminController.java              # Admin operations
|   |   |-- StudentController.java            # Student operations
|   |   |-- AuthController.java               # Authentication endpoints
|   |-- dto/
|   |   |-- StudentDTO.java                   # Student data transfer object
|   |   |-- PerformanceDTO.java               # Performance data transfer object
|   |-- model/
|   |   |-- Student.java                      # Student entity
|   |   |-- Course.java                       # Course entity
|   |   |-- Performance.java                  # Performance/Grade entity
|   |   |-- User.java                         # User authentication entity
|   |-- repository/
|   |   |-- StudentRepository.java            # Student data access
|   |   |-- CourseRepository.java             # Course data access
|   |   |-- PerformanceRepository.java        # Performance data access
|   |   |-- UserRepository.java               # User data access
|   |-- service/
|       |-- AnalyticsService.java             # Business logic for analytics
|       |-- StudentService.java               # Student management logic
|       |-- PerformanceService.java           # Performance management logic
|
|-- src/main/resources/
|   |-- application.yml                       # Application configuration
|   |-- templates/
|       |-- index.html                        # Basic Thymeleaf template
|
|-- pom.xml                                    # Maven configuration
|-- README.md                                  # This file
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE student_performance;
```

2. Update database credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_performance
    username: your_username
    password: your_password
```

### Running the Application
1. Navigate to the project directory:
```bash
cd StudentPerformanceAnalytics
```

2. Build and run the application:
```bash
mvn spring-boot:run
```

3. Access the application at: `http://localhost:8080`

## Default Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: ADMIN

### Student Users
- **Username**: `STU001`
- **Password**: `password`
- **Role**: STUDENT

- **Username**: `STU002`
- **Password**: `password`
- **Role**: STUDENT

## API Endpoints

### Authentication
- `GET /api/auth/profile` - Get user profile
- `GET /api/auth/role` - Get user role

### Admin Operations
- `GET /api/admin/students` - Get all students
- `GET /api/admin/students/{id}` - Get student by ID
- `POST /api/admin/students` - Create new student
- `PUT /api/admin/students/{id}` - Update student
- `DELETE /api/admin/students/{id}` - Delete student
- `GET /api/admin/performances` - Get all performance records
- `POST /api/admin/performances` - Create performance record
- `GET /api/admin/analytics/overview` - Get overall statistics
- `GET /api/admin/analytics/department/{department}` - Get department performance
- `GET /api/admin/analytics/at-risk` - Get at-risk students

### Student Operations
- `GET /api/student/profile/{studentId}` - Get student profile
- `GET /api/student/performances/{studentId}` - Get student performances
- `GET /api/student/performances/{studentId}/semester/{semester}` - Get performances by semester
- `GET /api/student/performances/{studentId}/year/{year}` - Get performances by year
- `GET /api/student/analytics/summary/{studentId}` - Get performance summary
- `GET /api/student/analytics/recommendations/{studentId}` - Get recommendations
- `GET /api/student/gpa/{studentId}` - Calculate GPA

## Sample Data

The application automatically initializes with:
- 2 sample students (John Doe, Jane Smith)
- 2 sample courses (CS101, CS201)
- Sample performance records with various grades

This provides a realistic environment for testing and demonstration.

## Development

The application includes Spring Boot DevTools for automatic restart during development. Simply make changes to the code and the application will restart automatically.

## Security

- Role-based access control using Spring Security
- HTTP Basic Authentication for API access
- In-memory user authentication (configurable for production)
- CSRF protection disabled for API endpoints

## Future Enhancements

- Web-based user interface
- Email notifications for performance alerts
- Export functionality for reports (PDF, Excel)
- Integration with learning management systems
- Advanced analytics and machine learning predictions
- Mobile application support

## License

This project is for educational purposes as part of a college project (FSAD-SDP-29).
