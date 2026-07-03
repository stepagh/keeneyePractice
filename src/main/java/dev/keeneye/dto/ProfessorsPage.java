package dev.keeneye.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record ProfessorsPage(
        List<ProfessorResponse> professors,
        boolean hasMore
) {
    public static ProfessorsPage from (Slice<ProfessorResponse> professorSlice) {
        return new ProfessorsPage(professorSlice.getContent(), professorSlice.hasNext());
    }
}
