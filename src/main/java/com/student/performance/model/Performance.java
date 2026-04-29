package com.student.performance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;
    
    @NotBlank(message = "Exam type is required")
    @Column(nullable = false)
    private String examType;
    
    @NotNull(message = "Obtained marks are required")
    @Min(value = 0, message = "Obtained marks cannot be negative")
    @Max(value = 1000, message = "Obtained marks cannot exceed 1000")
    @Column(nullable = false)
    private double obtainedMarks;
    
    @NotNull(message = "Maximum marks are required")
    @Min(value = 1, message = "Maximum marks must be at least 1")
    @Max(value = 1000, message = "Maximum marks cannot exceed 1000")
    @Column(nullable = false)
    private double maxMarks;
    
    @NotBlank(message = "Semester is required")
    @Column(nullable = false)
    private String semester;
    
    @NotNull(message = "Academic year is required")
    @Min(value = 2000, message = "Academic year must be 2000 or later")
    @Max(value = 2100, message = "Academic year must be 2100 or earlier")
    @Column(nullable = false)
    private int academicYear;
    
    @NotNull(message = "Exam date is required")
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
    
    public double getPercentage() {
        return maxMarks > 0 ? (obtainedMarks / maxMarks) * 100 : 0;
    }
}
