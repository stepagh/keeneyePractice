package dev.keeneye.repositories;

import dev.keeneye.entities.Professor;
import dev.keeneye.entities.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

        Slice<Professor> findAllBy(Pageable pageable);

        @Query("""
        SELECT p FROM Professor p
        JOIN FETCH p.groups g
        JOIN FETCH g.professors
        """)
        Optional<Professor> findByidFetchGroupsProfessors(Long id);

}
