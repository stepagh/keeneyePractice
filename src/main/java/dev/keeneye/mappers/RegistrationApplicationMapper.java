package dev.keeneye.mappers;

import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.entities.RegistrationApplication;
import dev.keeneye.enums.ApplicationStatus;
import dev.keeneye.enums.Role;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RegistrationApplicationMapper {
    /**
     * @throws IllegalArgumentException if role value doesn't align with enum values
     */
    default RegistrationApplication toEntity(UserCsvDto dto, long expirationHours) {
        if (dto == null) {
            return null;
        }
        Role role = Role.valueOf(dto.getRole().toUpperCase());
        String confirmationToken = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(expirationHours * 3600);
        RegistrationApplication application = new RegistrationApplication();
        application.setEmail(dto.getEmail());
        application.setFio(dto.getFio());
        application.setRole(role);
        application.setGroupName(dto.getGroupName());
        application.setConfirmationToken(confirmationToken);
        application.setExpiryDate(expiryDate);

        return application;
    }
}
