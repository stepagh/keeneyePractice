package dev.keeneye.dto;

import java.util.List;

public record ProfessorResponse(
        Long id,
        String fio,
        String phoneNumber,
        String email,
        List<ShortGroupInfo> groups) {
}
