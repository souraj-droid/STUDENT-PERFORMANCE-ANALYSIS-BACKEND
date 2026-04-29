package com.student.performance.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "timetables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String day;
    
    @Column(nullable = false)
    private int hour;
    
    @Column(nullable = false)
    private String courseCode;
    
    @Column(nullable = false)
    private String section;
    
    @Column
    private String roomNumber;
    
    @Column(nullable = false)
    private String academicYear;
}
