package dev.keeneye.dto;

import dev.keeneye.entities.Student;
import org.springframework.data.domain.Slice;

import java.util.List;

public record StudentsPage(
        List<Student> students,
        boolean hasMore
) {
    public static StudentsPage from(Slice<Student> studentSlice) {
        return new StudentsPage(studentSlice.getContent(), studentSlice.hasNext());
    }
}
