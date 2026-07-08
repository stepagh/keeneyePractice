package dev.keeneye.controllers;

import dev.keeneye.dto.*;
import dev.keeneye.entities.RefreshToken;
import dev.keeneye.entities.User;
import dev.keeneye.exceptions.InvalidFileFormatException;
import dev.keeneye.security.jwt.JwtCore;
import dev.keeneye.services.AuthService;
import dev.keeneye.services.CsvParserService;
import dev.keeneye.services.RefreshTokenService;
import dev.keeneye.services.RegistrationApplicationService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final CsvParserService csvParserService;
    private final RegistrationApplicationService applicationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CsvProcessingResult> uploadRegistrationCsv(@RequestParam MultipartFile file) {
        List<UserCsvDto> dtos = csvParserService.parseRegistrationCsv(file);
        return ResponseEntity.ok(applicationService.processRegistrationApplications(dtos));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
            applicationService.confirmRegistration(token);
            return ResponseEntity.ok("Регистрация успешно подтверждена");
    }
}