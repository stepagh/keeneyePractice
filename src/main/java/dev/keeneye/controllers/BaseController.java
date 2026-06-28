package dev.keeneye.controllers;

import dev.keeneye.dto.StudentPage;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Student;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.services.StudentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/base")
@RequiredArgsConstructor
public class BaseController {
    private final StudentService studentService;
    private final StudentMapper studentMapper;
    @GetMapping
    public String greetJava() {
        return "Hello world " + new Date();
    }


    @PostMapping("students")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody Student newStudent) {
        StudentResponse response = studentMapper.toResponse(studentService.createStudent(newStudent));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("students")
    public ResponseEntity<StudentResponse> changeStudent(@RequestBody Student changingStudent) {
        StudentResponse response = studentMapper.toResponse(studentService.updateStudent(changingStudent));
        return ResponseEntity.ok(response);
    }
    @GetMapping("students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> response = studentService.getAllStudents().stream().map(studentMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("students/page")
    public ResponseEntity<StudentPage> getStudentsPage(@RequestParam int offset, @RequestParam int limit) {
        StudentPage page = studentService.getStudentsPage(offset, limit);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        StudentResponse response = studentMapper.toResponse(studentService.getStudentById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("students/filter")
    public ResponseEntity<List<StudentResponse>> getStudentsByGroup(@RequestParam String group) {
        List<StudentResponse> response = studentService.getStudentsByGroup(group).stream().map(studentMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
}
