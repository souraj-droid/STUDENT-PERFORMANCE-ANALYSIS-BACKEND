package com.student.performance.service;

import com.student.performance.model.Student;
import com.student.performance.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setStudentId("STU001");
        testStudent.setEmail("john.doe@example.com");
        testStudent.setDepartment("CSE");
        testStudent.setAdmissionYear(2023);
        testStudent.setSemester("Fall");
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(testStudent));

        List<Student> students = studentService.getAllStudents();

        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        Optional<Student> student = studentService.getStudentById(1L);

        assertTrue(student.isPresent());
        assertEquals("John", student.get().getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student savedStudent = studentService.saveStudent(testStudent);

        assertNotNull(savedStudent);
        assertEquals("John", savedStudent.getFirstName());
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsByEmail() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = studentService.existsByEmail("john.doe@example.com");

        assertTrue(exists);
        verify(studentRepository, times(1)).existsByEmail("john.doe@example.com");
    }

    @Test
    void testExistsByStudentId() {
        when(studentRepository.existsByStudentId(anyString())).thenReturn(true);

        boolean exists = studentService.existsByStudentId("STU001");

        assertTrue(exists);
        verify(studentRepository, times(1)).existsByStudentId("STU001");
    }

    @Test
    void testSearchStudentsByName() {
        when(studentRepository.findByNameContaining(anyString()))
                .thenReturn(Arrays.asList(testStudent));

        List<Student> students = studentService.searchStudentsByName("John");

        assertNotNull(students);
        assertEquals(1, students.size());
        verify(studentRepository, times(1)).findByNameContaining("John");
    }
}
