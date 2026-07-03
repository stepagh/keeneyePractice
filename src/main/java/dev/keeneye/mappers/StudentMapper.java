package dev.keeneye.mappers;

import dev.keeneye.dto.StudentRequest;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "group", source = "studyGroup.name")
    StudentResponse toResponse(Student student);


    Student toEntity(StudentRequest studentRequest, Long id, Group group);

    @Mapping(target = "groupName", source = "studyGroup.name")
    StudentRequest toStudentRequest(Student student);
}
