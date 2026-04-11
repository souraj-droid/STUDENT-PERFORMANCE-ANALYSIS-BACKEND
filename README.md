# Student Performance Analytics and Reporting System

A comprehensive **full-stack** application for analyzing student performance data and generating reports with actionable insights for educators and students. Includes a React frontend and Spring Boot backend with MySQL database.

## Features

### Admin (Teacher) Operations
- **Student Management**: Create, read, update, and delete student records
- **Performance Data Input**: Record and manage student grades and exam results
- **Analytics Dashboard**: Comprehensive performance statistics and insights
- **Department-Level Reporting**: Track performance by academic departments
- **At-Risk Student Identification**: Identify students who need additional support
- **Performance Trend Analysis**: Monitor academic progress over time
- **Course Management**: View and manage courses
- **Timetable Management**: View class schedules
- **Report Generation**: Create detailed student reports (backend pending)

### Student Operations
- **Personal Performance Viewing**: Access individual academic records
- **Academic Progress Tracking**: Monitor semester-by-semester performance
- **GPA Calculation**: Automatic grade point average computation
- **Academic Standing**: Determine current academic status
- **Personalized Recommendations**: Get improvement suggestions based on performance
- **Subject Performance**: View detailed performance by subject
- **Timetable Access**: View class schedules
- **Report Viewing**: View generated reports (backend pending)

### System Capabilities
- **Role-Based Authentication**: Secure access control (TEACHER/STUDENT roles)
- **Database-Backed Authentication**: User credentials stored in MySQL
- **RESTful API**: 20+ endpoints for comprehensive data management
- **Advanced Analytics**: Intelligent performance analysis and reporting
- **MySQL Database**: Robust data persistence with JPA/Hibernate
- **Automatic Grade Calculation**: Built-in grade computation logic
- **Initial Data**: Pre-populated with 18 courses and timetable
- **Real-Time Data**: All frontend components use live database data

## Technology Stack

### Backend
- **Spring Boot 3.2.5** - Main application framework
- **Spring Web** - REST API development
- **Spring Data JPA** - Database access layer
- **Spring Security** - Authentication and authorization
- **MySQL** - Database management
- **Lombok** - Code generation and boilerplate reduction
- **Maven** - Build and dependency management

### Frontend
- **React 18** - UI framework
- **React Router** - Client-side routing
- **Recharts** - Data visualization charts
- **Lucide React** - Icon library
- **Tailwind CSS** - Utility-first CSS framework
- **Axios** - HTTP client (via fetch API)

## Project Structure

### Backend
```
StudentPerformanceAnalytics/
|
|-- src/main/java/com/student/performance/
|   |-- StudentPerformanceApplication.java     # Main application class
|   |-- config/
|   |   |-- SecurityConfig.java               # Security configuration
|   |   |-- CustomUserDetailsService.java     # Database authentication
|   |   |-- DataInitializer.java              # Initial data setup (18 courses, timetable)
|   |-- controller/
|   |   |-- AdminController.java              # Admin operations
|   |   |-- StudentController.java            # Student operations
|   |   |-- AuthController.java               # Authentication endpoints
|   |   |-- HomeController.java               # Root endpoint
|   |-- dto/
|   |   |-- StudentDTO.java                   # Student data transfer object
|   |   |-- PerformanceDTO.java               # Performance data transfer object
|   |-- model/
|   |   |-- Student.java                      # Student entity
|   |   |-- Course.java                       # Course entity
|   |   |-- Performance.java                  # Performance/Grade entity
|   |   |-- User.java                         # User authentication entity
|   |   |-- Timetable.java                    # Timetable entity
|   |-- repository/
|   |   |-- StudentRepository.java            # Student data access
|   |   |-- CourseRepository.java             # Course data access
|   |   |-- PerformanceRepository.java        # Performance data access
|   |   |-- UserRepository.java               # User data access
|   |   |-- TimetableRepository.java          # Timetable data access
|   |-- service/
|       |-- AnalyticsService.java             # Business logic for analytics
|       |-- StudentService.java               # Student management logic
|       |-- PerformanceService.java           # Performance management logic
|
|-- src/main/resources/
|   |-- application.yml                       # Application configuration
|
|-- pom.xml                                    # Maven configuration
|-- README.md                                  # This file
|-- BACKEND_ADAPTATION_GUIDE.md                # Complete backend implementation guide
|-- BACKEND_REPORT_REQUIREMENTS.md            # Report feature backend specs
|-- PROJECT_STATUS.md                          # Current project status
```

### Frontend
```
Student Performance Analytics System2/
|
|-- src/
|   |-- components/
|   |   |-- Login.jsx                         # Login page
|   |   |-- Navbar.jsx                        # Navigation bar
|   |   |-- Sidebar.jsx                       # Sidebar navigation
|   |   |-- TeacherDashboard.jsx              # Teacher dashboard
|   |   |-- StudentDashboard.jsx              # Student dashboard
|   |   |-- Analytics.jsx                     # Analytics page
|   |   |-- AddStudent.jsx                    # Add student form
|   |   |-- EditStudent.jsx                   # Edit student form
|   |   |-- AllStudents.jsx                   # All students list
|   |   |-- Subjects.jsx                      # Subjects display
|   |   |-- Timetable.jsx                     # Timetable display
|   |   |-- Report.jsx                        # Report generation/viewing
|   |   |-- StudentTable.jsx                  # Generic table component
|   |   |-- ChartCard.jsx                     # Chart wrapper component
|   |-- services/
|   |   |-- api.js                            # API service functions
|   |-- App.jsx                               # Main app component
|   |-- main.jsx                              # Entry point
|
|-- package.json                              # Frontend dependencies
```

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Node.js 18 or higher
- npm or yarn

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

### Backend Setup
1. Navigate to the backend project directory:
```bash
cd StudentPerformanceAnalytics
```

2. Build and run the application:
```bash
mvn spring-boot:run
```

3. Backend will be available at: `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend project directory:
```bash
cd "Student Performance Analytics System2"
```

2. Install dependencies:
```bash
npm install
```

3. Start the frontend development server:
```bash
npm start
```

4. Frontend will be available at: `http://localhost:3000`

## Default Credentials

### Teacher (Admin) User
- **Username**: `admin`
- **Password**: `admin`
- **Role**: TEACHER

### Student Users
- **Username**: `STU001` (auto-created when student is added)
- **Password**: `password`
- **Role**: STUDENT

Note: Student users are automatically created when a new student is added via the Add Student form. The default password is "password".

## API Endpoints

### Authentication
- `GET /api/auth/profile` - Get user profile

### Admin Operations
- `GET /api/admin/students` - Get all students
- `GET /api/admin/students/{id}` - Get student by ID
- `POST /api/admin/students` - Create new student (also creates User record)
- `PUT /api/admin/students/{id}` - Update student (also updates User record)
- `DELETE /api/admin/students/{id}` - Delete student (also deletes User record)
- `GET /api/admin/students/search?name={name}` - Search students by name
- `GET /api/admin/students/department/{department}` - Get students by department
- `GET /api/admin/students/year/{year}` - Get students by admission year
- `GET /api/admin/performances` - Get all performance records
- `POST /api/admin/performances` - Create performance record
- `GET /api/admin/analytics/overview` - Get overall statistics
- `GET /api/admin/analytics/department/{department}` - Get department performance
- `GET /api/admin/analytics/at-risk` - Get at-risk students
- `GET /api/admin/courses` - Get all courses
- `GET /api/admin/timetable` - Get timetable

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
- 18 courses covering various CS topics (Operating Systems, Data Structures, Database Systems, etc.)
- Weekly timetable (Mon-Sat, hours 1-24)
- Default admin user (username: admin, password: admin)

This provides a realistic environment for testing and demonstration. Students can be added via the Add Student form in the frontend.

## Development

### Backend
The application includes Spring Boot DevTools for automatic restart during development. Simply make changes to the code and the application will restart automatically.

### Frontend
The frontend uses React's development server with hot module replacement. Changes are reflected automatically in the browser.

## Security

- Role-based access control using Spring Security
- HTTP Basic Authentication for API access
- Database-backed user authentication (MySQL)
- CSRF protection disabled for API endpoints
- Passwords encrypted with BCrypt

## Future Enhancements

- Email notifications for performance alerts
- Export functionality for reports (PDF, Excel)
- Integration with learning management systems
- Advanced analytics and machine learning predictions
- Mobile application support
- Password change functionality for students
- Report feature backend implementation (frontend complete)

## License

This project is for educational purposes as part of a college project (FSAD-SDP-29).
#   S T U D E N T - P E R F O R M A N C E - A N A L Y S I S - B A C K E N D  
 