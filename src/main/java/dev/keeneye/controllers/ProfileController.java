package dev.keeneye.controllers;

import dev.keeneye.annotations.CurrentUserId;
import dev.keeneye.dto.ChangePasswordRequest;
import dev.keeneye.dto.UserResponse;
import dev.keeneye.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'PROFESSOR')")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request, @CurrentUserId Long id) {
        profileService.changePassword(request, id);
        return ResponseEntity.noContent().build();
    }
}
