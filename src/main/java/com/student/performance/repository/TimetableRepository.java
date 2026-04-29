package com.student.performance.repository;

import com.student.performance.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByDay(String day);

    List<Timetable> findByCourseCode(String courseCode);

    List<Timetable> findBySection(String section);

    List<Timetable> findByAcademicYear(String academicYear);

    @Query("SELECT t FROM Timetable t WHERE t.day = :day AND t.section = :section AND t.academicYear = :academicYear ORDER BY t.hour")
    List<Timetable> findByDayAndSectionAndAcademicYearOrderByHour(@Param("day") String day, @Param("section") String section, @Param("academicYear") String academicYear);

    @Query("SELECT t FROM Timetable t WHERE t.courseCode = :courseCode AND t.academicYear = :academicYear")
    List<Timetable> findByCourseCodeAndAcademicYear(@Param("courseCode") String courseCode, @Param("academicYear") String academicYear);
}
