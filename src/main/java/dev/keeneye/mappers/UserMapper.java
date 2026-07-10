package dev.keeneye.mappers;

import dev.keeneye.entities.RegistrationApplication;
import dev.keeneye.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", source = "fio")
    User toEntity(RegistrationApplication application);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "application.fio")
    @Mapping(target = "password", source = "tempPassword")
    User toEntity(RegistrationApplication application, String tempPassword);

}
