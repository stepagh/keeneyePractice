package dev.keeneye.repositories;

import dev.keeneye.entities.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);


    @Query("""
        SELECT g from Group g
        JOIN FETCH g.professors
        """)
    Optional<Group> findByIdFetchProfessors(Long id);

    Slice<Group> findAllBy(Pageable pageable);

    @Query("""
        SELECT g FROM Group g
        JOIN FETCH g.students
        JOIN FETCH g.professors
        """)
    Optional<Group> findByIdFetch(Long id);

    boolean existsByName(String name);
}
