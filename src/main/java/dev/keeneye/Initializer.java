package dev.keeneye;

import dev.keeneye.dto.Student;
import dev.keeneye.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Initializer {
    @Autowired
    private CustomerRepository customerRepository;

    public void initial() {
        customerRepository.save(new Student("fio", "def_group", "+79"));
    }

}
