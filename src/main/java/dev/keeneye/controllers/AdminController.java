package dev.keeneye.controllers;

import dev.keeneye.dto.*;
import dev.keeneye.services.AdminService;
import dev.keeneye.services.GroupService;
import dev.keeneye.services.ProfessorService;
import dev.keeneye.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final AdminService adminService;

    private final GroupService groupService;

    @GetMapping("/students")
    public ResponseEntity<StudentsPage> getAllStudentsPage(@RequestParam int offset, @RequestParam int limit) {
        return ResponseEntity.ok(studentService.getStudentPage(offset, limit));
    }

    @GetMapping("/professors")
    public ResponseEntity<ProfessorsPage> getAllProfessorsPage(@RequestParam int offset, @RequestParam int limit) {
        return ResponseEntity.ok(professorService.getAllProfessorsPage(offset, limit));
    }

    @PatchMapping("/professors/{professorId}/groups/{groupId}")
    public void assignProfessorToGroup(@PathVariable Long professorId, @PathVariable Long groupId) {
        adminService.assignProfessorToGroup(professorId, groupId);
    }

    @DeleteMapping("/professors/{professorId}/groups/{groupId}")
    public ResponseEntity<Void> removeProfessorFromGroup(@PathVariable Long professorId, @PathVariable Long groupId) {
        adminService.removeProfessorFromGroup(professorId, groupId);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.changeStudentById(id, request));
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/professors/{id}")
    public ResponseEntity<ProfessorResponse> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @PutMapping("/professors/{id}")
    public ResponseEntity<ProfessorResponse> updateProfessor(@PathVariable Long id, @Valid @RequestBody ProfessorRequest request) {
        return ResponseEntity.ok(professorService.updateProfessor(id, request));
    }

    @DeleteMapping("/professors/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/groups")
    public ResponseEntity<GroupsPage> getAllGroupsPage(@RequestParam int offset, @RequestParam int limit) {
        return ResponseEntity.ok(groupService.getAllGroupsPage(offset, limit));
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(dto));
    }

    @PatchMapping("/groups/{id}")
    public ResponseEntity<ShortGroupInfo> updateGroupName(@PathVariable Long id, @Valid @RequestBody GroupRequest request) {
        return ResponseEntity.ok(groupService.updateGroupName(id, request));
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteEmptyGroup(id);
        return ResponseEntity.noContent().build();
    }

}
