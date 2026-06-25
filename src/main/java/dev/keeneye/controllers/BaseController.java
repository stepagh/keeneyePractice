package dev.keeneye.controllers;

import dev.keeneye.dto.Student;
import dev.keeneye.repositories.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/base")
public class BaseController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String greetJava() {
        return "Hello world " + new Date();
    }

    @PostConstruct
    private void init() {
        customerRepository.save(new Student("User1", "VM", "+7"));
        customerRepository.save(new Student("User2", "VM", "+8"));
        customerRepository.save(new Student("User3", "AM", "+99"));
    }

    @PostMapping("students")
    public Student createStudent(@RequestBody Student newStudent) {
        return addStudent(newStudent);
    }

    private Student addStudent(Student student){
        student.setId(null);
        customerRepository.save(student);
        return student;
    }

    @PutMapping("students")
    public Student changeStudent(@RequestBody Student changingStudent) {
        return updateStudent(changingStudent);
    }

    private Student updateStudent(Student student) {
        if(student.getId() == null) {
            throw new RuntimeException("id of changing student cannot be null");
        }
        customerRepository.save(student);
        return student;
    }
    @GetMapping("students")
    public List<Student> getAllStudents() {
        return customerRepository.findAll();
    }

    @DeleteMapping("students/{id}")
    public Long deleteStudent(@PathVariable("id") Long id) {
        return removeStudent(id);
    }

    private Long removeStudent(Long id) {
        customerRepository.deleteById(id);
        return id;
    }

    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable("id") Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("student with id: " + id + " was not found"));
    }

    @GetMapping("students/filter")
    public Student getStudentByGroup(@RequestParam("group") String group) {
        return customerRepository.findByGroup(group)
                .orElse(null);
    }
}
