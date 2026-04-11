package com.student.performance.dto;

import com.student.performance.model.Student;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private int admissionYear;
    private String semester;
    
    public static StudentDTO fromEntity(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setDepartment(student.getDepartment());
        dto.setAdmissionYear(student.getAdmissionYear());
        dto.setSemester(student.getSemester());
        return dto;
    }
    
    public Student toEntity() {
        Student student = new Student();
        student.setId(this.id);
        student.setStudentId(this.studentId);
        student.setFirstName(this.firstName);
        student.setLastName(this.lastName);
        student.setEmail(this.email);
        student.setDepartment(this.department);
        student.setAdmissionYear(this.admissionYear);
        student.setSemester(this.semester);
        return student;
    }
}
