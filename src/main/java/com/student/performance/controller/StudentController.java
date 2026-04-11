package com.student.performance.controller;

import com.student.performance.model.Performance;
import com.student.performance.model.Student;
import com.student.performance.model.Report;
import com.student.performance.service.AnalyticsService;
import com.student.performance.service.StudentService;
import com.student.performance.service.PerformanceService;
import com.student.performance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentService studentService;
    private final PerformanceService performanceService;
    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    
    @GetMapping("/profile/{studentId}")
    public ResponseEntity<Student> getStudentProfile(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/performances/{studentId}")
    public ResponseEntity<List<Performance>> getStudentPerformances(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(performanceService.getPerformancesByStudent(student.get().getId()));
    }
    
    @GetMapping("/performances/{studentId}/semester/{semester}")
    public ResponseEntity<List<Performance>> getStudentPerformancesBySemester(
            @PathVariable String studentId, @PathVariable String semester) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(performanceService.getPerformancesByStudentAndSemester(
                student.get().getId(), semester));
    }
    
    @GetMapping("/performances/{studentId}/year/{year}")
    public ResponseEntity<List<Performance>> getStudentPerformancesByYear(
            @PathVariable String studentId, @PathVariable int year) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(performanceService.getPerformancesByStudentAndYear(
                student.get().getId(), year));
    }
    
    @GetMapping("/analytics/summary/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentPerformanceSummary(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(analyticsService.getStudentPerformanceSummary(student.get().getId()));
    }
    
    @GetMapping("/analytics/recommendations/{studentId}")
    public ResponseEntity<List<String>> getStudentRecommendations(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(analyticsService.getRecommendationsForStudent(student.get().getId()));
    }
    
    @GetMapping("/gpa/{studentId}")
    public ResponseEntity<Map<String, Object>> calculateGPA(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<Performance> performances = performanceService.getPerformancesByStudent(student.get().getId());
        Map<String, Object> gpaInfo = Map.of(
            "studentId", studentId,
            "studentName", student.get().getFullName(),
            "totalCourses", performances.size(),
            "averageGrade", performanceService.getAveragePerformanceForStudent(student.get().getId()),
            "failedCourses", performanceService.getFailedCoursesCountForStudent(student.get().getId()),
            "academicStanding", getAcademicStanding(performances)
        );

        return ResponseEntity.ok(gpaInfo);
    }

    @GetMapping("/reports")
    public ResponseEntity<Report> getMyReport(@RequestParam String studentId) {
        return reportService.getReportByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    private String getAcademicStanding(List<Performance> performances) {
        if (performances.isEmpty()) {
            return "No Data";
        }
        
        double averageGrade = performances.stream()
                .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                .average().orElse(0.0);
        
        long failedCourses = performances.stream()
                .filter(p -> "F".equals(p.getGrade()))
                .count();
        
        if (averageGrade >= 75 && failedCourses == 0) {
            return "Excellent";
        } else if (averageGrade >= 60 && failedCourses <= 1) {
            return "Good";
        } else if (averageGrade >= 50 && failedCourses <= 2) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }
}
