package dev.keeneye.repositories;

import dev.keeneye.entities.RegistrationApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RegistrationApplicationRepository extends JpaRepository<RegistrationApplication, Long> {
    boolean existsByEmail(String email);
    Optional<RegistrationApplication> findByConfirmationToken(String confirmationToken);



    long deleteAllByExpiryDateBefore(Instant now);

    boolean existsByGroupName(String groupName);
}
