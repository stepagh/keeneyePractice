package dev.keeneye.dto;

public record StudentResponse(
        Long id,
        String fio,
        String group,
        String phoneNumber,
        String email) {}