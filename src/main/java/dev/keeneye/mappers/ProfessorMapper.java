package dev.keeneye.mappers;

import dev.keeneye.dto.ProfessorResponse;
import dev.keeneye.dto.ShortGroupInfo;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    ProfessorResponse toResponse(Professor professor);

    ShortGroupInfo toShortGroupInfo(Group group);
}
