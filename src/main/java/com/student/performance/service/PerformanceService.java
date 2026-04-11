package com.student.performance.service;

import com.student.performance.model.Performance;
import com.student.performance.repository.PerformanceRepository;
import com.student.performance.repository.StudentRepository;
import com.student.performance.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    
    private final PerformanceRepository performanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    
    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }
    
    public Optional<Performance> getPerformanceById(Long id) {
        return performanceRepository.findById(id);
    }
    
    public Performance savePerformance(Performance performance) {
        return performanceRepository.save(performance);
    }
    
    public void deletePerformance(Long id) {
        performanceRepository.deleteById(id);
    }
    
    public List<Performance> getPerformancesByStudent(Long studentId) {
        return performanceRepository.findByStudentId(studentId);
    }
    
    public List<Performance> getPerformancesByCourse(Long courseId) {
        return performanceRepository.findByCourseId(courseId);
    }
    
    public List<Performance> getPerformancesByExamType(String examType) {
        return performanceRepository.findByExamType(examType);
    }
    
    public List<Performance> getPerformancesBySemester(String semester) {
        return performanceRepository.findBySemester(semester);
    }
    
    public List<Performance> getPerformancesByAcademicYear(int year) {
        return performanceRepository.findByAcademicYear(year);
    }
    
    public List<Performance> getPerformancesByStudentAndSemester(Long studentId, String semester) {
        return performanceRepository.findByStudentAndSemester(studentId, semester);
    }
    
    public List<Performance> getPerformancesByStudentAndYear(Long studentId, int year) {
        return performanceRepository.findByStudentAndYear(studentId, year);
    }
    
    public Double getAveragePerformanceForStudent(Long studentId) {
        return performanceRepository.getAveragePerformanceByStudent(studentId);
    }
    
    public Double getAveragePerformanceForCourse(Long courseId) {
        return performanceRepository.getAveragePerformanceByCourse(courseId);
    }
    
    public long getFailedCoursesCountForStudent(Long studentId) {
        return performanceRepository.countFailedCoursesByStudent(studentId);
    }
    
    public boolean validateStudentAndCourse(Long studentId, Long courseId) {
        return studentRepository.existsById(studentId) && courseRepository.existsById(courseId);
    }
}
