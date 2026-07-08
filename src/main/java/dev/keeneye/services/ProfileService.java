package dev.keeneye.services;

import dev.keeneye.dto.ChangePasswordRequest;
import dev.keeneye.dto.UserResponse;
import dev.keeneye.entities.User;
import dev.keeneye.exceptions.IncorrectPasswordException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void changePassword(ChangePasswordRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String currPasswordRequest = passwordEncoder.encode(request.currentPassword());
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Wrongly entered current password");
        }
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password is the same as current password");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
