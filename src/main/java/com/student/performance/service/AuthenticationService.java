package com.student.performance.service;

import com.student.performance.dto.LoginRequest;
import com.student.performance.dto.LoginResponse;
import com.student.performance.model.Student;
import com.student.performance.model.User;
import com.student.performance.repository.StudentRepository;
import com.student.performance.repository.UserRepository;
import com.student.performance.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        // Get additional user info
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long studentId = user.getStudentId();
        String studentName = null;
        String department = null;
        String semester = null;

        if (studentId != null && "STUDENT".equals(user.getRole())) {
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student != null) {
                studentName = student.getFullName();
                department = student.getDepartment();
                semester = student.getSemester();
            }
        }

        return LoginResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .studentId(studentId)
                .studentName(studentName)
                .department(department)
                .semester(semester)
                .message("Login successful")
                .success(true)
                .build();
    }
}
