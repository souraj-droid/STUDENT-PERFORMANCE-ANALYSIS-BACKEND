package com.student.performance.repository;

import com.student.performance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentId(String studentId);
    
    boolean existsByStudentId(String studentId);
    
    Optional<Student> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Student> findByDepartment(String department);
    
    List<Student> findByAdmissionYear(int admissionYear);
    
    List<Student> findBySemester(String semester);
    
    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.department = :department")
    long countByDepartment(@Param("department") String department);
    
    @Query("SELECT s FROM Student s WHERE s.admissionYear = :year AND s.semester = :semester")
    List<Student> findByAdmissionYearAndSemester(@Param("year") int year, @Param("semester") String semester);
}
