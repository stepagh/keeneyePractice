package dev.keeneye.dto;

import dev.keeneye.enums.Role;
import lombok.Data;

public record RegisterRequest(
        String username,
        String password,
        Role role
) {}
