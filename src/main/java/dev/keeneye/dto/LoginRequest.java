package dev.keeneye.dto;

import org.jspecify.annotations.Nullable;

public record LoginRequest(
        String username,
        String password
) {}