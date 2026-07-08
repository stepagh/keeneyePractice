package dev.keeneye.entities;

import dev.keeneye.dto.UserCsvDto;
import dev.keeneye.enums.ApplicationStatus;
import dev.keeneye.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "registration_applications")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false, unique = true)
    private String confirmationToken;

    @Column(nullable = false)
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    public RegistrationApplication(String email, String fio, Role role, String groupName, String confirmationToken, Instant expiryDate, ApplicationStatus status) {
        this.email = email;
        this.fio = fio;
        this.role = role;
        this.groupName = groupName;
        this.confirmationToken = confirmationToken;
        this.expiryDate = expiryDate;
        this.status = status;
    }


    public static RegistrationApplication from (UserCsvDto dto, long expirationHours) {
        Role role = Role.valueOf(dto.getRole().toUpperCase());
        String confirmationToken = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusSeconds(expirationHours * 3600);

        return new RegistrationApplication(dto.getEmail(), dto.getFio(), role, dto.getGroupName(), confirmationToken,
                expiryDate, ApplicationStatus.PENDING);
    }
}