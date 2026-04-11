package com.student.performance.dto;

import com.student.performance.model.Performance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String examType;
    private double obtainedMarks;
    private double maxMarks;
    private String semester;
    private int academicYear;
    private LocalDate examDate;
    private String grade;
    private String remarks;
    
    public static PerformanceDTO fromEntity(Performance performance) {
        PerformanceDTO dto = new PerformanceDTO();
        dto.setId(performance.getId());
        dto.setStudentId(performance.getStudent().getId());
        dto.setStudentName(performance.getStudent().getFullName());
        dto.setCourseId(performance.getCourse().getId());
        dto.setCourseName(performance.getCourse().getCourseName());
        dto.setExamType(performance.getExamType());
        dto.setObtainedMarks(performance.getObtainedMarks());
        dto.setMaxMarks(performance.getMaxMarks());
        dto.setSemester(performance.getSemester());
        dto.setAcademicYear(performance.getAcademicYear());
        dto.setExamDate(performance.getExamDate());
        dto.setRemarks(performance.getRemarks());
        return dto;
    }
}
