package dev.keeneye.services;

import dev.keeneye.dto.JwtResponse;
import dev.keeneye.dto.LoginRequest;
import dev.keeneye.dto.RegisterRequest;
import dev.keeneye.entities.*;
import dev.keeneye.exceptions.UsernameAlreadyExistsException;
import dev.keeneye.repositories.AdminRepository;
import dev.keeneye.repositories.ProfessorRepository;
import dev.keeneye.repositories.StudentRepository;
import dev.keeneye.repositories.UserRepository;
import dev.keeneye.security.jwt.JwtCore;
import dev.keeneye.security.user.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;
    private final RefreshTokenService refreshTokenService;

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtCore.generateToken(authentication);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser().getId());

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();


        return new JwtResponse(jwt, refreshToken.getToken(), username, role);
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with this username already exists");
        }

        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(registerRequest.role())
                .build();

        userRepository.save(user);
        switch (registerRequest.role()) {
            case ROLE_STUDENT -> {
                Student student = new Student();
                student.setUser(user);
                studentRepository.save(student);
            }
            case ROLE_PROFESSOR -> {
                Professor professor = new Professor();
                professor.setUser(user);
                professorRepository.save(professor);
            }
            case ROLE_ADMIN -> {
                Administrator admin = new Administrator();
                admin.setUser(user);
                admin.setUsername(user.getUsername());
                adminRepository.save(admin);
            }
        }
    }
}
