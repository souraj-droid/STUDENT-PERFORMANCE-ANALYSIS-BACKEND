package com.student.performance.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "performances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false)
    private String examType;
    
    @Column(nullable = false)
    private double obtainedMarks;
    
    @Column(nullable = false)
    private double maxMarks;
    
    @Column(nullable = false)
    private String semester;
    
    @Column(nullable = false)
    private int academicYear;
    
    @Column(nullable = false)
    private LocalDate examDate;
    
    private String grade;
    
    private String remarks;
    
    @PrePersist
    @PreUpdate
    public void calculateGrade() {
        if (maxMarks > 0) {
            double percentage = (obtainedMarks / maxMarks) * 100;
            
            if (percentage >= 90) {
                grade = "A+";
            } else if (percentage >= 80) {
                grade = "A";
            } else if (percentage >= 70) {
                grade = "B+";
            } else if (percentage >= 60) {
                grade = "B";
            } else if (percentage >= 50) {
                grade = "C";
            } else if (percentage >= 40) {
                grade = "D";
            } else {
                grade = "F";
            }
        }
    }
    
    public double getGrade() {
        calculateGrade();
        return maxMarks > 0 ? (obtainedMarks / maxMarks) * 100 : 0;
    }
}
