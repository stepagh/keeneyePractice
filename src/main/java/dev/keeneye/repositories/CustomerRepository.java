package dev.keeneye.repositories;

import dev.keeneye.dto.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Student, Long> {

    List<Student> findAll();
    Optional<Student> findByGroup(String group);
}
