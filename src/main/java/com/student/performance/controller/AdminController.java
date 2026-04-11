package com.student.performance.controller;

import com.student.performance.model.Course;
import com.student.performance.model.Student;
import com.student.performance.model.Performance;
import com.student.performance.model.Timetable;
import com.student.performance.model.User;
import com.student.performance.model.Report;
import com.student.performance.model.ReportSubject;
import com.student.performance.dto.ReportDTO;
import com.student.performance.dto.ReportSubjectDTO;
import com.student.performance.repository.CourseRepository;
import com.student.performance.repository.TimetableRepository;
import com.student.performance.repository.UserRepository;
import com.student.performance.service.AnalyticsService;
import com.student.performance.service.StudentService;
import com.student.performance.service.PerformanceService;
import com.student.performance.service.ReportService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        if (studentService.existsByEmail(student.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        if (studentService.existsByStudentId(student.getStudentId())) {
            return ResponseEntity.badRequest().build();
        }
        if (userRepository.existsByUsername(student.getStudentId())) {
            return ResponseEntity.badRequest().build();
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
        
        return ResponseEntity.ok(savedStudent);
    }
    
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        if (!studentService.getStudentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Student existingStudent = studentService.getStudentById(id).get();
        
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
        
        return ResponseEntity.ok(savedStudent);
    }
    
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentService.getStudentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentService.getStudentById(id).get();
        
        // Delete associated user account
        userRepository.findByStudentId(id).ifPresent(userRepository::delete);
        
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/performances")
    public ResponseEntity<List<Performance>> getAllPerformances() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }
    
    @PostMapping("/performances")
    public ResponseEntity<Performance> createPerformance(@RequestBody Performance performance) {
        if (!performanceService.validateStudentAndCourse(
                performance.getStudent().getId(), 
                performance.getCourse().getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(performanceService.savePerformance(performance));
    }
    
    @GetMapping("/analytics/overview")
    public ResponseEntity<Map<String, Object>> getOverallStatistics() {
        return ResponseEntity.ok(analyticsService.getOverallStatistics());
    }
    
    @GetMapping("/analytics/department/{department}")
    public ResponseEntity<Map<String, Object>> getDepartmentPerformance(@PathVariable String department) {
        return ResponseEntity.ok(analyticsService.getDepartmentPerformance(department));
    }
    
    @GetMapping("/analytics/at-risk")
    public ResponseEntity<List<Map<String, Object>>> getAtRiskStudents() {
        return ResponseEntity.ok(analyticsService.getAtRiskStudents());
    }
    
    @GetMapping("/students/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/students/department/{department}")
    public ResponseEntity<List<Student>> getStudentsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(studentService.getStudentsByDepartment(department));
    }
    
    @GetMapping("/students/year/{year}")
    public ResponseEntity<List<Student>> getStudentsByAdmissionYear(@PathVariable int year) {
        return ResponseEntity.ok(studentService.getStudentsByAdmissionYear(year));
    }
    
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }
    
    @GetMapping("/timetable")
    public ResponseEntity<List<Timetable>> getTimetable() {
        return ResponseEntity.ok(timetableRepository.findAll());
    }

    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@RequestBody ReportDTO reportDTO) {
        // Map DTO to Report entity
        Report report = new Report();
        report.setName(reportDTO.getName());
        report.setRollNo(reportDTO.getRollNo());
        report.setClassSection(reportDTO.getClassSection());
        report.setAcademicYear(reportDTO.getAcademicYear());
        report.setOverallPercentage(reportDTO.getOverallPercentage());
        report.setGrade(reportDTO.getGrade());
        report.setCreatedAt(LocalDate.now());

        // Set student
        if (reportDTO.getStudentId() != null) {
            Student student = studentService.getStudentById(reportDTO.getStudentId()).orElse(null);
            report.setStudent(student);
        }

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

        return ResponseEntity.ok(reportService.createReport(report));
    }

    @GetMapping("/reports/{studentId}")
    public ResponseEntity<List<Report>> getStudentReports(@PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getReportsByStudentId(studentId));
    }
}
