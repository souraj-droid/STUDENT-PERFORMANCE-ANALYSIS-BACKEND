package com.student.performance.config;

import com.student.performance.model.Course;
import com.student.performance.model.Timetable;
import com.student.performance.model.User;
import com.student.performance.model.Student;
import com.student.performance.model.Performance;
import com.student.performance.repository.CourseRepository;
import com.student.performance.repository.TimetableRepository;
import com.student.performance.repository.UserRepository;
import com.student.performance.repository.StudentRepository;
import com.student.performance.repository.PerformanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TimetableRepository timetableRepository;
    private final StudentRepository studentRepository;
    private final PerformanceRepository performanceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CourseRepository courseRepository, TimetableRepository timetableRepository, StudentRepository studentRepository, PerformanceRepository performanceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.timetableRepository = timetableRepository;
        this.studentRepository = studentRepository;
        this.performanceRepository = performanceRepository;
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
        
        // Create sample performance data for students
        if (performanceRepository.count() == 0) {
            createSamplePerformanceData();
            System.out.println("Sample performance data created");
        }
        
        System.out.println("Data initialization completed");
    }
    
    private void createSamplePerformanceData() {
        // Get students and courses
        List<Student> students = studentRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        
        if (students.isEmpty() || courses.isEmpty()) {
            return;
        }
        
        String[] examTypes = {"Midterm", "Final", "Quiz", "Assignment"};
        String[] semesters = {"Even Sem"};
        int[] years = {2025, 2026};
        
        // Create performance data for each student
        for (Student student : students) {
            // Add 6 random course performances for each student
            for (int i = 0; i < 6 && i < courses.size(); i++) {
                Course course = courses.get(i);
                
                Performance perf = new Performance();
                perf.setStudent(student);
                perf.setCourse(course);
                perf.setExamType(examTypes[(int)(Math.random() * examTypes.length)]);
                perf.setMaxMarks(100);
                // Random score between 40 and 95
                perf.setObtainedMarks(40 + Math.random() * 55);
                perf.setSemester(semesters[0]);
                perf.setAcademicYear(years[(int)(Math.random() * years.length)]);
                perf.setExamDate(LocalDate.now().minusDays((int)(Math.random() * 60)));
                perf.setRemarks("Sample data for graphs");
                
                performanceRepository.save(perf);
            }
        }
    }
    
    private void createCourses() {
        String[][] courseData = {
            {"24CS2101L", "OPERATING SYSTEMS", "CSE", "L", "S-56-MA", "2025-2026", "Even Sem"},
            {"24CS2101P", "OPERATING SYSTEMS", "CSE", "P", "S-56-B", "2025-2026", "Even Sem"},
            {"24MT2012L", "MATHEMATICAL OPTIMIZATION", "CSE", "L", "S-64-MA", "2025-2026", "Even Sem"},
            {"24MT2012T", "MATHEMATICAL OPTIMIZATION", "CSE", "T", "S-64-MA", "2025-2026", "Even Sem"},
            {"24CS2203L", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "L", "S-75-MA", "2025-2026", "Even Sem"},
            {"24CS2203P", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "P", "S-75-A", "2025-2026", "Even Sem"},
            {"24CS2203S", "DESIGN AND ANALYSIS OF ALGORITHMS", "CSE", "S", "S-75-A", "2025-2026", "Even Sem"},
            {"24SDCS02L", "FULL STACK APPLICATION DEVELOPMENT", "CSE", "L", "S-61-MA", "2025-2026", "Even Sem"},
            {"24SDCS02S", "FULL STACK APPLICATION DEVELOPMENT", "CSE", "S", "S-61-B", "2025-2026", "Even Sem"},
            {"24CS2204L", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "L", "S-70-MA", "2025-2026", "Even Sem"},
            {"24CS2204P", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "P", "S-70-B", "2025-2026", "Even Sem"},
            {"24CS2204S", "CLOUD INFRASTRUCTURE AND SERVICES", "CSE", "S", "S-70-B", "2025-2026", "Even Sem"},
            {"24CS2255FL", "ADVANCED DATA STRUCTURES", "CSE", "L", "S-53-MA", "2025-2026", "Even Sem"},
            {"24CS2255FP", "ADVANCED DATA STRUCTURES", "CSE", "P", "S-53-B", "2025-2026", "Even Sem"},
            {"24IN2202L", "EMBEDDED SYSTEMS DESIGN", "CSE", "L", "S-51-MA", "2025-2026", "Even Sem"},
            {"24IN2202P", "EMBEDDED SYSTEMS DESIGN", "CSE", "P", "S-51-A", "2025-2026", "Even Sem"},
            {"24SP2102S", "BASKETBALL", "CSE", "S", "S-52-MA", "2025-2026", "Even Sem"},
            {"24CC3010S", "AWS CERTIFIED CLOUD PRACTITIONER", "CSE", "S", "S-57-MA", "2025-2026", "Even Sem"}
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
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        String[] courseCodes = {"24CS2101L", "24MT2012L", "24CS2203L", "24SDCS02L", "24CS2204L", "24CS2255FL", "24IN2202L", "24SP2102S", "24CC3010S"};
        String[] sections = {"L", "P", "T", "S"};
        String[] sectionNumbers = {"S-51", "S-52", "S-53", "S-56", "S-57", "S-61", "S-64", "S-70", "S-75"};
        String[] roomNumbers = {"C410", "C411", "C412", "C421", "C422", "C423", "C510", "C511", "C512", "C608", "C617", "C618", "L307", "L309", "L501", "R105A", "R306A", "C011", "C219"};
        
        // Generate 60 random timetable entries
        for (int i = 0; i < 60; i++) {
            String day = days[(int)(Math.random() * days.length)];
            int hour = 1 + (int)(Math.random() * 20); // Hours 1-20
            String courseCode = courseCodes[(int)(Math.random() * courseCodes.length)];
            String section = sections[(int)(Math.random() * sections.length)];
            String sectionNumber = sectionNumbers[(int)(Math.random() * sectionNumbers.length)];
            String roomNumber = roomNumbers[(int)(Math.random() * roomNumbers.length)];
            
            addTimetableEntry(day, hour, courseCode, section, sectionNumber, roomNumber, "2025-2026");
        }
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
