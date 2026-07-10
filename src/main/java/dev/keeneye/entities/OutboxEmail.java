package dev.keeneye.entities;

import dev.keeneye.enums.OutboxEmailType;
import dev.keeneye.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "outbox_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxEmailType type;

    @Column(nullable = false)
    private String email;

    private String confirmationToken;


    private String username;
    private String tempPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status = OutboxStatus.PENDING;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant processedAt;

    private int retryCount = 0;

    public OutboxEmail(String email, String confirmationToken) {
        this.email = email;
        this.confirmationToken = confirmationToken;
        this.type = OutboxEmailType.REGISTRATION_CONFIRMATION;
    }

    public OutboxEmail(String email, String username, String tempPassword) {
        this.email = email;
        this.username = username;
        this.tempPassword = tempPassword;
        this.type = OutboxEmailType.REGISTRATION_SUCCESS;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
