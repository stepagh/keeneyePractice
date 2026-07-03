package dev.keeneye.controllers;

import dev.keeneye.dto.StudentsPage;
import dev.keeneye.dto.StudentRequest;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.services.ProfessorService;
import dev.keeneye.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/professors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorController {
    private final ProfessorService professorService;
    private final StudentService studentService;

    @GetMapping("/{id}/students")
    public ResponseEntity<StudentsPage> getProfessorsStudents(@PathVariable Long professorId, @RequestParam int offset, @RequestParam int limit) {
        return ResponseEntity.ok(professorService.getProfessorsStudents(professorId, offset, limit));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> changeStudentById(@PathVariable Long studentId,@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.changeStudentById(studentId, request));
    }

}
