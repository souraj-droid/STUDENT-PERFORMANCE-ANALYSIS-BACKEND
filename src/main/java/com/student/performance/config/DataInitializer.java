package com.student.performance.config;

import com.student.performance.model.Course;
import com.student.performance.model.Timetable;
import com.student.performance.model.User;
import com.student.performance.repository.CourseRepository;
import com.student.performance.repository.TimetableRepository;
import com.student.performance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TimetableRepository timetableRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CourseRepository courseRepository, TimetableRepository timetableRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.timetableRepository = timetableRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) {
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@school.edu");
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Admin user created: admin/admin123");
        }
        
        // Create student users if not exist
        if (!userRepository.existsByUsername("STU001")) {
            User student1 = new User();
            student1.setUsername("STU001");
            student1.setPassword(passwordEncoder.encode("password"));
            student1.setEmail("student1@school.edu");
            student1.setRole("STUDENT");
            student1.setEnabled(true);
            student1.setStudentId(1L);
            userRepository.save(student1);
            System.out.println("Student user created: STU001/password");
        }
        
        if (!userRepository.existsByUsername("STU002")) {
            User student2 = new User();
            student2.setUsername("STU002");
            student2.setPassword(passwordEncoder.encode("password"));
            student2.setEmail("student2@school.edu");
            student2.setRole("STUDENT");
            student2.setEnabled(true);
            student2.setStudentId(2L);
            userRepository.save(student2);
            System.out.println("Student user created: STU002/password");
        }
        
        // Create courses if not exist
        if (courseRepository.count() == 0) {
            createCourses();
            System.out.println("18 courses created");
        }
        
        // Create timetable if not exist
        if (timetableRepository.count() == 0) {
            createTimetable();
            System.out.println("Timetable created");
        }
        
        System.out.println("Data initialization completed");
    }
    
    private void createCourses() {
        String[][] courseData = {
            {"24CS2101", "OPERATING SYSTEMS", "CSE", "L", "S-56-MA", "2025-2026", "Even Sem"},
            {"24CS2101", "OPERATING SYSTEMS", "CSE", "P", "S-56-B", "2025-2026", "Even Sem"},
            {"24MT2012", "MATHEMATICAL OPTIMIZATION", "CSE", "L", "S-64-MA", "2025-2026", "Even Sem"},
            {"24MT2012", "MATHEMATICAL OPTIMIZATION", "CSE", "T", "S-64-MA", "2025-2026", "Even Sem"},
            {"24CS2203", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "L", "S-75-MA", "2025-2026", "Even Sem"},
            {"24CS2203", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "P", "S-75-A", "2025-2026", "Even Sem"},
            {"24CS2203", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "S", "S-75-A", "2025-2026", "Even Sem"},
            {"24SDCS02", "FULL STACK APPLICATION DEVELOPMENT", "CSE", "L", "S-61-MA", "2025-2026", "Even Sem"},
            {"24SDCS02", "FULL STACK APPLICATION DEVELOPMENT", "CSE", "S", "S-61-B", "2025-2026", "Even Sem"},
            {"24CS2204", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "L", "S-70-MA", "2025-2026", "Even Sem"},
            {"24CS2204", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "P", "S-70-B", "2025-2026", "Even Sem"},
            {"24CS2204", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "S", "S-70-B", "2025-2026", "Even Sem"},
            {"24CS2255F", "ADVANCED DATA STRUCTURES", "CSE", "L", "S-53-MA", "2025-2026", "Even Sem"},
            {"24CS2255F", "ADVANCED DATA STRUCTURES", "CSE", "P", "S-53-B", "2025-2026", "Even Sem"},
            {"24IN2202", "EMBEDDED SYSTEMS DESIGN", "CSE", "L", "S-51-MA", "2025-2026", "Even Sem"},
            {"24IN2202", "EMBEDDED SYSTEMS DESIGN", "CSE", "P", "S-51-A", "2025-2026", "Even Sem"},
            {"24SP2102", "BASKETBALL", "CSE", "S", "S-52-MA", "2025-2026", "Even Sem"},
            {"24CC3010", "AWS CERTIFIED CLOUD PRACTITIONER", "CSE", "S", "S-57-MA", "2025-2026", "Even Sem"}
        };
        
        for (String[] data : courseData) {
            Course course = new Course();
            course.setCourseCode(data[0]);
            course.setCourseName(data[1]);
            course.setDepartment(data[2]);
            course.setSection(data[3]);
            course.setRoom(data[4]);
            course.setAcademicYear(data[5]);
            course.setSemester(data[6]);
            course.setCredits(3); // Default credits
            courseRepository.save(course);
        }
    }
    
    private void createTimetable() {
        // Monday
        addTimetableEntry("Mon", 3, "24MT2012", "T", "S-64", "C410", "2025-2026");
        addTimetableEntry("Mon", 4, "24MT2012", "T", "S-64", "C410", "2025-2026");
        addTimetableEntry("Mon", 5, "24CS2255F", "L", "S-53", "C421B1", "2025-2026");
        addTimetableEntry("Mon", 6, "24CS2255F", "L", "S-53", "C421B1", "2025-2026");
        addTimetableEntry("Mon", 13, "24IN2202", "L", "S-51", "C421A", "2025-2026");
        addTimetableEntry("Mon", 14, "24IN2202", "L", "S-51", "C421A", "2025-2026");
        addTimetableEntry("Mon", 15, "24CS2204", "L", "S-70", "C011", "2025-2026");
        addTimetableEntry("Mon", 16, "24CS2204", "L", "S-70", "C011", "2025-2026");
        addTimetableEntry("Mon", 17, "24CS2203", "S", "S-75", "C618", "2025-2026");
        addTimetableEntry("Mon", 18, "24CS2203", "S", "S-75", "C618", "2025-2026");
        addTimetableEntry("Mon", 19, "24CS2203", "S", "S-75", "C618", "2025-2026");
        addTimetableEntry("Mon", 20, "24CS2203", "S", "S-75", "C618", "2025-2026");
        
        // Tuesday
        addTimetableEntry("Tue", 1, "24CC3010", "S", "S-57", "C422", "2025-2026");
        addTimetableEntry("Tue", 2, "24CC3010", "S", "S-57", "C422", "2025-2026");
        addTimetableEntry("Tue", 3, "24CC3010", "S", "S-57", "C422", "2025-2026");
        addTimetableEntry("Tue", 4, "24CC3010", "S", "S-57", "C422", "2025-2026");
        addTimetableEntry("Tue", 5, "24CS2203", "P", "S-75", "R306A", "2025-2026");
        addTimetableEntry("Tue", 6, "24CS2203", "P", "S-75", "R306A", "2025-2026");
        addTimetableEntry("Tue", 7, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 8, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 9, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 10, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 11, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 12, "24CS2101", "L", "S-56", "L307", "2025-2026");
        addTimetableEntry("Tue", 15, "24SDCS02", "S", "S-61", "C510", "2025-2026");
        addTimetableEntry("Tue", 16, "24SDCS02", "S", "S-61", "C510", "2025-2026");
        addTimetableEntry("Tue", 17, "24SDCS02", "S", "S-61", "C510", "2025-2026");
        addTimetableEntry("Tue", 18, "24SDCS02", "S", "S-61", "C510", "2025-2026");
        addTimetableEntry("Tue", 19, "24CC3010", "S", "S-57", "C511", "2025-2026");
        addTimetableEntry("Tue", 20, "24CC3010", "S", "S-57", "C511", "2025-2026");
        addTimetableEntry("Tue", 21, "24CC3010", "S", "S-57", "C511", "2025-2026");
        addTimetableEntry("Tue", 22, "24CC3010", "S", "S-57", "C511", "2025-2026");
        
        // Wednesday
        addTimetableEntry("Wed", 1, "24IN2202", "L", "S-51", "C221B2", "2025-2026");
        addTimetableEntry("Wed", 2, "24IN2202", "L", "S-51", "C221B2", "2025-2026");
        addTimetableEntry("Wed", 3, "24IN2202", "L", "S-51", "C221B2", "2025-2026");
        addTimetableEntry("Wed", 4, "24IN2202", "L", "S-51", "C221B2", "2025-2026");
        addTimetableEntry("Wed", 5, "24SDCS02", "S", "S-61", "L309", "2025-2026");
        addTimetableEntry("Wed", 6, "24SDCS02", "S", "S-61", "L309", "2025-2026");
        addTimetableEntry("Wed", 7, "24SDCS02", "S", "S-61", "L309", "2025-2026");
        addTimetableEntry("Wed", 8, "24SDCS02", "S", "S-61", "L309", "2025-2026");
        addTimetableEntry("Wed", 9, "24MT2012", "L", "S-64", "C421B1", "2025-2026");
        addTimetableEntry("Wed", 10, "24MT2012", "L", "S-64", "C421B1", "2025-2026");
        addTimetableEntry("Wed", 11, "24MT2012", "L", "S-64", "C421B1", "2025-2026");
        addTimetableEntry("Wed", 12, "24MT2012", "L", "S-64", "C421B1", "2025-2026");
        addTimetableEntry("Wed", 15, "24CS2203", "L", "S-75", "C608", "2025-2026");
        addTimetableEntry("Wed", 16, "24CS2203", "L", "S-75", "C608", "2025-2026");
        addTimetableEntry("Wed", 17, "24CS2203", "L", "S-75", "C608", "2025-2026");
        addTimetableEntry("Wed", 18, "24CS2203", "L", "S-75", "C608", "2025-2026");
        addTimetableEntry("Wed", 19, "24CS2204", "S", "S-70", "S603", "2025-2026");
        addTimetableEntry("Wed", 20, "24CS2204", "S", "S-70", "S603", "2025-2026");
        addTimetableEntry("Wed", 21, "24CS2204", "S", "S-70", "S603", "2025-2026");
        addTimetableEntry("Wed", 22, "24CS2204", "S", "S-70", "S603", "2025-2026");
        
        // Thursday
        addTimetableEntry("Thu", 3, "24CS2255F", "P", "S-53", "C617", "2025-2026");
        addTimetableEntry("Thu", 4, "24CS2255F", "P", "S-53", "C617", "2025-2026");
        addTimetableEntry("Thu", 5, "24CS2204", "P", "S-70", "C617", "2025-2026");
        addTimetableEntry("Thu", 6, "24CS2204", "P", "S-70", "C617", "2025-2026");
        addTimetableEntry("Thu", 7, "24CS2204", "P", "S-70", "C617", "2025-2026");
        addTimetableEntry("Thu", 8, "24CS2204", "P", "S-70", "C617", "2025-2026");
        addTimetableEntry("Thu", 11, "24CS2101", "L", "S-56", "C409", "2025-2026");
        addTimetableEntry("Thu", 12, "24CS2101", "L", "S-56", "C409", "2025-2026");
        
        // Friday
        addTimetableEntry("Fri", 1, "24IN2202", "P", "S-51", "R105A", "2025-2026");
        addTimetableEntry("Fri", 2, "24IN2202", "P", "S-51", "R105A", "2025-2026");
        addTimetableEntry("Fri", 3, "24IN2202", "P", "S-51", "R105A", "2025-2026");
        addTimetableEntry("Fri", 4, "24IN2202", "P", "S-51", "R105A", "2025-2026");
        addTimetableEntry("Fri", 5, "24CS2101", "P", "S-56", "C219", "2025-2026");
        addTimetableEntry("Fri", 6, "24CS2101", "P", "S-56", "C219", "2025-2026");
        addTimetableEntry("Fri", 7, "24CS2101", "P", "S-56", "C219", "2025-2026");
        addTimetableEntry("Fri", 8, "24CS2101", "P", "S-56", "C219", "2025-2026");
        addTimetableEntry("Fri", 9, "24CS2204", "L", "S-70", "L501", "2025-2026");
        addTimetableEntry("Fri", 10, "24CS2204", "L", "S-70", "L501", "2025-2026");
        addTimetableEntry("Fri", 11, "24CS2204", "L", "S-70", "L501", "2025-2026");
        addTimetableEntry("Fri", 12, "24CS2204", "L", "S-70", "L501", "2025-2026");
        addTimetableEntry("Fri", 15, "24CS2203", "L", "S-75", "C423", "2025-2026");
        addTimetableEntry("Fri", 16, "24SDCS02", "L", "S-61", "C110", "2025-2026");
        addTimetableEntry("Fri", 17, "24SP2102", "S", "S-52", "BASKETBALL COURT", "2025-2026");
        addTimetableEntry("Fri", 18, "24SP2102", "S", "S-52", "BASKETBALL COURT", "2025-2026");
        addTimetableEntry("Fri", 19, "24SP2102", "S", "S-52", "BASKETBALL COURT", "2025-2026");
        addTimetableEntry("Fri", 20, "24SP2102", "S", "S-52", "BASKETBALL COURT", "2025-2026");
    }
    
    private void addTimetableEntry(String day, int hour, String courseCode, String section, String sectionNumber, String roomNumber, String academicYear) {
        Timetable timetable = new Timetable();
        timetable.setDay(day);
        timetable.setHour(hour);
        timetable.setCourseCode(courseCode);
        timetable.setSection(section);
        timetable.setRoomNumber(sectionNumber + " - " + roomNumber);
        timetable.setAcademicYear(academicYear);
        timetableRepository.save(timetable);
    }
}
