package dev.keeneye.mappers;

import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toResponse(Student student);
}
