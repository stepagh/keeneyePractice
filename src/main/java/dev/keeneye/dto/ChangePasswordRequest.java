package dev.keeneye.dto;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
