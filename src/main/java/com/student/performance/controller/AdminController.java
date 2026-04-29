package com.student.performance.controller;

import com.student.performance.dto.ApiResponse;
import com.student.performance.exception.ResourceNotFoundException;
import com.student.performance.model.Course;
import com.student.performance.model.Student;
import com.student.performance.model.Performance;
import com.student.performance.model.Timetable;
import com.student.performance.model.User;
import com.student.performance.model.Report;
import com.student.performance.model.ReportSubject;
import com.student.performance.dto.ReportDTO;
import com.student.performance.repository.CourseRepository;
import com.student.performance.repository.TimetableRepository;
import com.student.performance.repository.UserRepository;
import com.student.performance.service.AnalyticsService;
import com.student.performance.service.StudentService;
import com.student.performance.service.PerformanceService;
import com.student.performance.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final StudentService studentService;
    private final PerformanceService performanceService;
    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TimetableRepository timetableRepository;
    private final ReportService reportService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students, "Students retrieved successfully"));
    }
    
    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return ResponseEntity.ok(ApiResponse.success(student, "Student retrieved successfully"));
    }
    
    @PostMapping("/students")
    public ResponseEntity<ApiResponse<Student>> createStudent(@Valid @RequestBody Student student) {
        if (studentService.existsByEmail(student.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email already exists"));
        }
        if (studentService.existsByStudentId(student.getStudentId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Student ID already exists"));
        }
        if (userRepository.existsByUsername(student.getStudentId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Username already exists"));
        }
        
        Student savedStudent = studentService.saveStudent(student);
        
        // Create user account for the student with default password
        User user = new User();
        user.setUsername(student.getStudentId());
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail(student.getEmail());
        user.setRole("STUDENT");
        user.setEnabled(true);
        user.setStudentId(savedStudent.getId());
        userRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedStudent, "Student created successfully"));
    }
    
    @PutMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Student>> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        Student existingStudent = studentService.getStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        // Check if studentId or email is being changed
        boolean studentIdChanged = !existingStudent.getStudentId().equals(student.getStudentId());
        boolean emailChanged = !existingStudent.getEmail().equals(student.getEmail());
        
        student.setId(id);
        Student savedStudent = studentService.saveStudent(student);
        
        // Update associated user account if studentId or email changed
        userRepository.findByStudentId(id).ifPresent(user -> {
            if (studentIdChanged) {
                user.setUsername(student.getStudentId());
            }
            if (emailChanged) {
                user.setEmail(student.getEmail());
            }
            userRepository.save(user);
        });
        
        return ResponseEntity.ok(ApiResponse.success(savedStudent, "Student updated successfully"));
    }
    
    @DeleteMapping("/students/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        // Delete associated user account
        userRepository.findByStudentId(id).ifPresent(userRepository::delete);
        
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student deleted successfully"));
    }
    
    @GetMapping("/performances")
    public ResponseEntity<ApiResponse<List<Performance>>> getAllPerformances() {
        List<Performance> performances = performanceService.getAllPerformances();
        return ResponseEntity.ok(ApiResponse.success(performances, "Performances retrieved successfully"));
    }
    
    @PostMapping("/performances")
    public ResponseEntity<ApiResponse<Performance>> createPerformance(@Valid @RequestBody Performance performance) {
        if (!performanceService.validateStudentAndCourse(
                performance.getStudent().getId(), 
                performance.getCourse().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid student or course ID"));
        }
        Performance savedPerformance = performanceService.savePerformance(performance);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedPerformance, "Performance created successfully"));
    }
    
    @GetMapping("/analytics/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOverallStatistics() {
        Map<String, Object> stats = analyticsService.getOverallStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Analytics overview retrieved successfully"));
    }
    
    @GetMapping("/analytics/department/{department}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDepartmentPerformance(@PathVariable String department) {
        Map<String, Object> stats = analyticsService.getDepartmentPerformance(department);
        return ResponseEntity.ok(ApiResponse.success(stats, "Department performance retrieved successfully"));
    }
    
    @GetMapping("/analytics/at-risk")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAtRiskStudents() {
        List<Map<String, Object>> atRiskStudents = analyticsService.getAtRiskStudents();
        return ResponseEntity.ok(ApiResponse.success(atRiskStudents, "At-risk students retrieved successfully"));
    }
    
    @GetMapping("/students/search")
    public ResponseEntity<ApiResponse<List<Student>>> searchStudents(@RequestParam @jakarta.validation.constraints.NotBlank String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(ApiResponse.success(students, "Students search completed"));
    }
    
    @GetMapping("/students/department/{department}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByDepartment(@PathVariable String department) {
        List<Student> students = studentService.getStudentsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success(students, "Students by department retrieved"));
    }
    
    @GetMapping("/students/year/{year}")
    public ResponseEntity<ApiResponse<List<Student>>> getStudentsByAdmissionYear(@PathVariable int year) {
        List<Student> students = studentService.getStudentsByAdmissionYear(year);
        return ResponseEntity.ok(ApiResponse.success(students, "Students by admission year retrieved"));
    }
    
    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(courses, "Courses retrieved successfully"));
    }
    
    @GetMapping("/timetable")
    public ResponseEntity<ApiResponse<List<Timetable>>> getTimetable() {
        List<Timetable> timetables = timetableRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(timetables, "Timetable retrieved successfully"));
    }

    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<Report>> createReport(@Valid @RequestBody ReportDTO reportDTO) {
        // Validate student exists
        Student student = studentService.getStudentById(reportDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", reportDTO.getStudentId()));
        
        // Map DTO to Report entity
        Report report = new Report();
        report.setName(reportDTO.getName());
        report.setRollNo(reportDTO.getRollNo());
        report.setClassSection(reportDTO.getClassSection());
        report.setAcademicYear(reportDTO.getAcademicYear());
        report.setOverallPercentage(reportDTO.getOverallPercentage());
        report.setGrade(reportDTO.getGrade());
        report.setCreatedAt(LocalDate.now());
        report.setStudent(student);

        // Map subjects
        if (reportDTO.getSubjects() != null && !reportDTO.getSubjects().isEmpty()) {
            List<ReportSubject> subjects = reportDTO.getSubjects().stream()
                .map(subjectDTO -> {
                    ReportSubject subject = new ReportSubject();
                    subject.setSubject(subjectDTO.getSubject());
                    subject.setMidterm(subjectDTO.getMidterm());
                    subject.setFinalExam(subjectDTO.getFinalExam());
                    subject.setAssignment(subjectDTO.getAssignment());
                    subject.setPractical(subjectDTO.getPractical());
                    subject.setTotal(subjectDTO.getTotal());
                    subject.setGrade(subjectDTO.getGrade());
                    subject.setReport(report);
                    return subject;
                })
                .collect(Collectors.toList());
            report.setSubjects(subjects);
        }

        Report savedReport = reportService.createReport(report);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedReport, "Report created successfully"));
    }

    @GetMapping("/reports/{studentId}")
    public ResponseEntity<ApiResponse<List<Report>>> getStudentReports(@PathVariable Long studentId) {
        // Validate student exists
        studentService.getStudentById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        List<Report> reports = reportService.getReportsByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success(reports, "Student reports retrieved successfully"));
    }
}
