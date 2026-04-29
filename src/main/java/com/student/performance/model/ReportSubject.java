package com.student.performance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "report_subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id")
    @JsonIgnore
    private Report report;

    private String subject;
    private Double midterm;
    private Double finalExam;
    private Double assignment;
    private Double practical;
    private Double total;
    private String grade;
}
