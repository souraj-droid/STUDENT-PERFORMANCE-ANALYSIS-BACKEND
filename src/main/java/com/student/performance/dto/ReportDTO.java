package com.student.performance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Roll number is required")
    @Size(min = 3, max = 20, message = "Roll number must be between 3 and 20 characters")
    private String rollNo;
    
    @NotBlank(message = "Class section is required")
    private String classSection;
    
    @NotBlank(message = "Academic year is required")
    private String academicYear;
    
    private String overallPercentage;
    
    private String grade;
    
    @Valid
    @NotNull(message = "Subjects are required")
    @Size(min = 1, message = "At least one subject is required")
    private List<ReportSubjectDTO> subjects;
}
