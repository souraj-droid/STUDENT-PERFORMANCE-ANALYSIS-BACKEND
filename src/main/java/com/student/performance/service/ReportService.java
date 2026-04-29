package com.student.performance.service;

import com.student.performance.model.Report;
import com.student.performance.repository.ReportRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report createReport(@NonNull Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getReportsByStudentId(Long studentId) {
        return reportRepository.findByStudentId(studentId);
    }

    public Optional<Report> getReportByStudentId(String studentId) {
        return reportRepository.findByStudentStudentId(studentId);
    }
}
