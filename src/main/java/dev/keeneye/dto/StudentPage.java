package dev.keeneye.dto;

import dev.keeneye.entities.Student;
import org.springframework.data.domain.Slice;

import java.util.List;

public record StudentPage(
        List<Student> students,
        boolean hasMore
) {
    public static StudentPage from(Slice<Student>studentSlice) {
        return new StudentPage(studentSlice.getContent(), studentSlice.hasNext());
    }
}
