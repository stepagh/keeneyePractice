package dev.keeneye.dto;

public record JwtResponse(
        String accesToken,
        String refreshToken,
        String username,
        String role
) {}
