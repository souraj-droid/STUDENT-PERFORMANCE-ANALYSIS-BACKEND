package com.student.performance.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long studentId;
    private String name;
    private String rollNo;
    private String classSection;
    private String academicYear;
    private String overallPercentage;
    private String grade;
    private List<ReportSubjectDTO> subjects;
}
