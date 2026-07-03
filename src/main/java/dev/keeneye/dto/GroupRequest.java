package dev.keeneye.dto;

import jakarta.validation.constraints.NotBlank;

public record GroupRequest(
        @NotBlank
        String name
) {
}
