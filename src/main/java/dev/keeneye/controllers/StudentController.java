package dev.keeneye.controllers;

import dev.keeneye.annotations.CurrentUserId;
import dev.keeneye.dto.StudentRequest;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Student;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    private final StudentService studentService;
    private final StudentMapper studentMapper;


    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateProfile(@Valid @RequestBody StudentRequest request, @PathVariable Long id, @CurrentUserId Long userId) {
        Student student = studentService.updateProfile(request, id, userId);
        return ResponseEntity.ok(studentMapper.toResponse(student));
    }

    @GetMapping("/{id}/group")
    public ResponseEntity<List<StudentResponse>> getClassmates(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getClassmates(studentId));
    }

}
