package com.student.performance.service;

import com.student.performance.model.Student;
import com.student.performance.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }
    
    public List<Student> getStudentsByAdmissionYear(int year) {
        return studentRepository.findByAdmissionYear(year);
    }
    
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContaining(name);
    }
    
    public boolean existsByEmail(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }
    
    public boolean existsByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).isPresent();
    }
}
