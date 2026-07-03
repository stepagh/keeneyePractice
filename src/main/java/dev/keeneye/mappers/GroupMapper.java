package dev.keeneye.mappers;

import dev.keeneye.dto.GroupDto;
import dev.keeneye.dto.ShortGroupInfo;
import dev.keeneye.entities.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, ProfessorMapper.class})
public interface GroupMapper {

    ShortGroupInfo toGroupInfo(Group group);


    GroupDto toDto(Group group);
}
