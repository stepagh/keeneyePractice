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


}