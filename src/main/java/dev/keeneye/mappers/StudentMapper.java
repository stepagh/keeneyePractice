package dev.keeneye.mappers;

import dev.keeneye.dto.StudentRequest;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.RegistrationApplication;
import dev.keeneye.entities.Student;
import dev.keeneye.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "group", source = "studyGroup.name")
    @Mapping(target = "email", source = "user.email")
    StudentResponse toResponse(Student student);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "studyGroup", source = "group")
    Student toEntity(StudentRequest studentRequest, Long id, Group group, User user);

    @Mapping(target = "id", ignore = true)
    Student toEntity(RegistrationApplication application, User user, Group studyGroup);

    @Mapping(target = "groupName", source = "studyGroup.name")
    @Mapping(target = "email", source = "user.email")
    StudentRequest toStudentRequest(Student student);
}
