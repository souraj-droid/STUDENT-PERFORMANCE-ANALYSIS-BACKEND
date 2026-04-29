package com.student.performance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String courseCode;
    
    @Column(nullable = false)
    private String courseName;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private int credits;
    
    @Column(nullable = false)
    private String semester;
    
    @Column(length = 1)
    private String section;
    
    @Column
    private String room;
    
    @Column
    private String academicYear;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Performance> performances;
    
    public int getStudentCount() {
        return performances != null ? performances.size() : 0;
    }
    
    public double getAveragePerformance() {
        if (performances == null || performances.isEmpty()) {
            return 0.0;
        }
        return performances.stream()
                .mapToDouble(Performance::getPercentage)
                .average()
                .orElse(0.0);
    }
}
