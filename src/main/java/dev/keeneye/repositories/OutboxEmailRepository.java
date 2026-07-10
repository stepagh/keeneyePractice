package dev.keeneye.repositories;

import dev.keeneye.entities.OutboxEmail;
import dev.keeneye.enums.OutboxStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEmailRepository extends JpaRepository<OutboxEmail, Long> {

    List<OutboxEmail> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
