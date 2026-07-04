package dev.keeneye.dto;


import dev.keeneye.validation.ValidEmail;
import dev.keeneye.validation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudentRequest(
        @NotBlank
        String fio,

        @NotBlank
        @ValidPhoneNumber
        String phoneNumber,

        @NotBlank
        String groupName,

        @NotBlank
        @ValidEmail
        String email
) {}