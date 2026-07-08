package dev.keeneye.dto;

import com.opencsv.bean.CsvBindByName;
import dev.keeneye.enums.Role;
import dev.keeneye.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCsvDto {
    @NotBlank
    private String fio;
    @NotBlank
    @ValidEmail
    private String email;
    @NotBlank
    private String role;
    @NotBlank
    private String groupName;
}
