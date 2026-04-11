package com.student.performance.repository;

import com.student.performance.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    List<Performance> findByStudentId(Long studentId);
    
    List<Performance> findByCourseId(Long courseId);
    
    List<Performance> findByExamType(String examType);
    
    List<Performance> findBySemester(String semester);
    
    List<Performance> findByAcademicYear(int academicYear);
    
    @Query("SELECT p FROM Performance p WHERE p.student.id = :studentId AND p.course.id = :courseId")
    List<Performance> findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT p FROM Performance p WHERE p.student.id = :studentId AND p.semester = :semester")
    List<Performance> findByStudentAndSemester(@Param("studentId") Long studentId, @Param("semester") String semester);
    
    @Query("SELECT p FROM Performance p WHERE p.student.id = :studentId AND p.academicYear = :year")
    List<Performance> findByStudentAndYear(@Param("studentId") Long studentId, @Param("year") int year);
    
    @Query("SELECT AVG(p.obtainedMarks / p.maxMarks * 100) FROM Performance p WHERE p.student.id = :studentId")
    Double getAveragePerformanceByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT AVG(p.obtainedMarks / p.maxMarks * 100) FROM Performance p WHERE p.course.id = :courseId")
    Double getAveragePerformanceByCourse(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(p) FROM Performance p WHERE p.student.id = :studentId AND p.grade = 'F'")
    long countFailedCoursesByStudent(@Param("studentId") Long studentId);
}
