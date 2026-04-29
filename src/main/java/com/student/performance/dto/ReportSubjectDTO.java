package com.student.performance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSubjectDTO {
    
    @NotBlank(message = "Subject name is required")
    private String subject;
    
    @Min(value = 0, message = "Midterm score cannot be negative")
    @Max(value = 100, message = "Midterm score cannot exceed 100")
    private Double midterm;
    
    @Min(value = 0, message = "Final exam score cannot be negative")
    @Max(value = 100, message = "Final exam score cannot exceed 100")
    private Double finalExam;
    
    @Min(value = 0, message = "Assignment score cannot be negative")
    @Max(value = 100, message = "Assignment score cannot exceed 100")
    private Double assignment;
    
    @Min(value = 0, message = "Practical score cannot be negative")
    @Max(value = 100, message = "Practical score cannot exceed 100")
    private Double practical;
    
    @Min(value = 0, message = "Total score cannot be negative")
    @Max(value = 100, message = "Total score cannot exceed 100")
    private Double total;
    
    private String grade;
}
