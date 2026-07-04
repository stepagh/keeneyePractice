package dev.keeneye.dto;

import dev.keeneye.validation.ValidEmail;
import dev.keeneye.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfessorRequest(
        @NotBlank
        String fio,
        @NotBlank(message = "Телефон обязателен")

        @ValidPhoneNumber
        String phoneNumber,

        @NotBlank(message = "Почта обязательна")
        @ValidEmail
        String email) {
}
