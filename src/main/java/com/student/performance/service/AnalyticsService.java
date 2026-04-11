package com.student.performance.service;

import com.student.performance.model.Performance;
import com.student.performance.model.Student;
import com.student.performance.repository.PerformanceRepository;
import com.student.performance.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final PerformanceRepository performanceRepository;
    private final StudentRepository studentRepository;
    
    public Map<String, Object> getStudentPerformanceSummary(Long studentId) {
        List<Performance> performances = performanceRepository.findByStudentId(studentId);
        
        if (performances.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCourses", performances.size());
        summary.put("averageGrade", performances.stream()
                .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                .average().orElse(0.0));
        summary.put("failedCourses", performances.stream()
                .filter(p -> "F".equals(p.getGrade()))
                .count());
        summary.put("highestGrade", performances.stream()
                .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                .max().orElse(0.0));
        summary.put("lowestGrade", performances.stream()
                .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                .min().orElse(0.0));
        
        return summary;
    }
    
    public Map<String, Object> getDepartmentPerformance(String department) {
        List<Student> students = studentRepository.findByDepartment(department);
        List<Performance> allPerformances = new ArrayList<>();
        
        for (Student student : students) {
            allPerformances.addAll(performanceRepository.findByStudentId(student.getId()));
        }
        
        Map<String, Object> deptPerformance = new HashMap<>();
        deptPerformance.put("totalStudents", students.size());
        deptPerformance.put("totalPerformances", allPerformances.size());
        
        if (!allPerformances.isEmpty()) {
            deptPerformance.put("averageGrade", allPerformances.stream()
                    .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                    .average().orElse(0.0));
            deptPerformance.put("passRate", (allPerformances.size() - 
                    allPerformances.stream().filter(p -> "F".equals(p.getGrade())).count()) * 100.0 / allPerformances.size());
        }
        
        return deptPerformance;
    }
    
    public List<Map<String, Object>> getAtRiskStudents() {
        List<Student> allStudents = studentRepository.findAll();
        List<Map<String, Object>> atRiskStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            List<Performance> performances = performanceRepository.findByStudentId(student.getId());
            
            if (!performances.isEmpty()) {
                double averageGrade = performances.stream()
                        .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                        .average().orElse(0.0);
                
                long failedCourses = performances.stream()
                        .filter(p -> "F".equals(p.getGrade()))
                        .count();
                
                if (averageGrade < 50 || failedCourses > 2) {
                    Map<String, Object> riskInfo = new HashMap<>();
                    riskInfo.put("studentId", student.getId());
                    riskInfo.put("studentName", student.getFullName());
                    riskInfo.put("studentNumber", student.getStudentId());
                    riskInfo.put("averageGrade", averageGrade);
                    riskInfo.put("failedCourses", failedCourses);
                    riskInfo.put("department", student.getDepartment());
                    
                    atRiskStudents.add(riskInfo);
                }
            }
        }
        
        return atRiskStudents.stream()
                .sorted((a, b) -> Double.compare((Double) b.get("failedCourses"), (Double) a.get("failedCourses")))
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalStudents = studentRepository.count();
        long totalPerformances = performanceRepository.count();
        
        stats.put("totalStudents", totalStudents);
        stats.put("totalPerformances", totalPerformances);
        
        if (totalPerformances > 0) {
            List<Performance> allPerformances = performanceRepository.findAll();
            double averageGrade = allPerformances.stream()
                    .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                    .average().orElse(0.0);
            
            stats.put("overallAverage", averageGrade);
            stats.put("passRate", (totalPerformances - 
                    allPerformances.stream().filter(p -> "F".equals(p.getGrade())).count()) * 100.0 / totalPerformances);
        }
        
        return stats;
    }
    
    public List<String> getRecommendationsForStudent(Long studentId) {
        List<Performance> performances = performanceRepository.findByStudentId(studentId);
        List<String> recommendations = new ArrayList<>();
        
        if (performances.isEmpty()) {
            recommendations.add("No performance data available. Please ensure grades are recorded.");
            return recommendations;
        }
        
        double averageGrade = performances.stream()
                .mapToDouble(p -> (p.getObtainedMarks() / p.getMaxMarks()) * 100)
                .average().orElse(0.0);
        
        long failedCourses = performances.stream()
                .filter(p -> "F".equals(p.getGrade()))
                .count();
        
        if (averageGrade < 60) {
            recommendations.add("Focus on improving overall academic performance. Consider additional study time.");
        }
        
        if (failedCourses > 0) {
            recommendations.add("Retake failed courses to improve academic standing.");
        }
        
        if (averageGrade >= 60 && averageGrade < 75) {
            recommendations.add("Good progress! Consider joining study groups for better performance.");
        }
        
        if (averageGrade >= 75) {
            recommendations.add("Excellent performance! Consider advanced courses or leadership roles.");
        }
        
        return recommendations;
    }
}
