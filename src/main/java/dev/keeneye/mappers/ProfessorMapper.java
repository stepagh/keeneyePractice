package dev.keeneye.mappers;

import dev.keeneye.dto.ProfessorResponse;
import dev.keeneye.dto.ShortGroupInfo;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Professor;
import dev.keeneye.entities.RegistrationApplication;
import dev.keeneye.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    @Mapping(target = "email", source = "user.email")
    ProfessorResponse toResponse(Professor professor);
    @Mapping(target = "groups", expression = "java(java.util.List.of(studyGroup))")
    @Mapping(target = "id", ignore = true)
    Professor toEntity(RegistrationApplication application, User user, Group studyGroup);

    ShortGroupInfo toShortGroupInfo(Group group);
}
