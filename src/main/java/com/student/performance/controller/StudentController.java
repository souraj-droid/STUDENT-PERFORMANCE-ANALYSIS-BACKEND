package com.student.performance.controller;

import com.student.performance.dto.ApiResponse;
import com.student.performance.exception.ResourceNotFoundException;
import com.student.performance.model.Performance;
import com.student.performance.model.Student;
import com.student.performance.model.Report;
import com.student.performance.service.AnalyticsService;
import com.student.performance.service.StudentService;
import com.student.performance.service.PerformanceService;
import com.student.performance.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentService studentService;
    private final PerformanceService performanceService;
    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    
    @GetMapping("/profile/{studentId}")
    public ResponseEntity<ApiResponse<Student>> getStudentProfile(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        return ResponseEntity.ok(ApiResponse.success(student, "Student profile retrieved successfully"));
    }
    
    @GetMapping("/performances/{studentId}")
    public ResponseEntity<ApiResponse<List<Performance>>> getStudentPerformances(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        List<Performance> performances = performanceService.getPerformancesByStudent(student.getId());
        return ResponseEntity.ok(ApiResponse.success(performances, "Student performances retrieved successfully"));
    }
    
    @GetMapping("/performances/{studentId}/semester/{semester}")
    public ResponseEntity<ApiResponse<List<Performance>>> getStudentPerformancesBySemester(
            @PathVariable String studentId, @PathVariable String semester) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        List<Performance> performances = performanceService.getPerformancesByStudentAndSemester(
                student.getId(), semester);
        return ResponseEntity.ok(ApiResponse.success(performances, "Semester performances retrieved successfully"));
    }
    
    @GetMapping("/performances/{studentId}/year/{year}")
    public ResponseEntity<ApiResponse<List<Performance>>> getStudentPerformancesByYear(
            @PathVariable String studentId, @PathVariable int year) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        List<Performance> performances = performanceService.getPerformancesByStudentAndYear(
                student.getId(), year);
        return ResponseEntity.ok(ApiResponse.success(performances, "Year performances retrieved successfully"));
    }
    
    @GetMapping("/analytics/summary/{studentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentPerformanceSummary(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        Map<String, Object> summary = analyticsService.getStudentPerformanceSummary(student.getId());
        return ResponseEntity.ok(ApiResponse.success(summary, "Performance summary retrieved successfully"));
    }
    
    @GetMapping("/analytics/recommendations/{studentId}")
    public ResponseEntity<ApiResponse<List<String>>> getStudentRecommendations(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
        List<String> recommendations = analyticsService.getRecommendationsForStudent(student.getId());
        return ResponseEntity.ok(ApiResponse.success(recommendations, "Recommendations retrieved successfully"));
    }
    
    @GetMapping("/gpa/{studentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateGPA(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));

        List<Performance> performances = performanceService.getPerformancesByStudent(student.getId());
        Map<String, Object> gpaInfo = Map.of(
            "studentId", studentId,
            "studentName", student.getFullName(),
            "totalCourses", performances.size(),
            "averageGrade", performanceService.getAveragePerformanceForStudent(student.getId()),
            "failedCourses", performanceService.getFailedCoursesCountForStudent(student.getId()),
            "academicStanding", getAcademicStanding(performances)
        );

        return ResponseEntity.ok(ApiResponse.success(gpaInfo, "GPA calculated successfully"));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<Report>> getMyReport(@RequestParam String studentId) {
        Report report = reportService.getReportByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "studentId", studentId));
        return ResponseEntity.ok(ApiResponse.success(report, "Report retrieved successfully"));
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
