package com.student.performance.repository;

import com.student.performance.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCourseCode(String courseCode);
    
    List<Course> findByDepartment(String department);
    
    List<Course> findBySemester(String semester);
    
    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %:name%")
    List<Course> findByCourseNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.department = :department")
    long countByDepartment(@Param("department") String department);
    
    @Query("SELECT c FROM Course c WHERE c.credits >= :minCredits AND c.credits <= :maxCredits")
    List<Course> findByCreditsBetween(@Param("minCredits") int minCredits, @Param("maxCredits") int maxCredits);
}
