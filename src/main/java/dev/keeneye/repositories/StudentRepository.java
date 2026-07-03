package dev.keeneye.repositories;

import dev.keeneye.entities.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Slice<Student> findAllBy(Pageable pageable);
    List<Student> findByStudyGroup_Name(String group);


    @Query("""
    SELECT s from Student s
        JOIN FETCH s.studyGroup g
        JOIN g.professors p
        WHERE p.id = :professorId
    """)
    Slice<Student> findAllStudentsByProfessorId(Long professorId, Pageable pageable);

}
