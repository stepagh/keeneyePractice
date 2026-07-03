package dev.keeneye.dto;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {}
