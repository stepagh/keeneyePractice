package dev.keeneye.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudentRequest(
        @NotBlank
        String fio,
        @NotBlank
        @Pattern(
                regexp = "^\\+7\\d{10}$",
                message = "Телефон должен быть в формате +7XXXXXXXXXX"
        )
        String phoneNumber,
        @NotBlank
        String groupName,
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Почта должна быть в формате xx@xx.xx"
        )
        String email
) {}