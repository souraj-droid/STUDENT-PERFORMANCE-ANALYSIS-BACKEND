package com.student.performance.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSubjectDTO {
    private String subject;
    private Double midterm;
    private Double finalExam;
    private Double assignment;
    private Double practical;
    private Double total;
    private String grade;
}
