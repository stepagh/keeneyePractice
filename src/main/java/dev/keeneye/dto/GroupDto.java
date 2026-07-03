package dev.keeneye.dto;

import java.util.List;

public record GroupDto(
        Long id,
        String name,
        List<StudentResponse> students,
        List<ProfessorResponse> professors
) {}
