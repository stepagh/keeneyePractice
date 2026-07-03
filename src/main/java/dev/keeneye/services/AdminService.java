package dev.keeneye.services;

import dev.keeneye.entities.Group;
import dev.keeneye.entities.Professor;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.repositories.GroupRepository;
import dev.keeneye.repositories.ProfessorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final GroupRepository groupRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public void assignProfessorToGroup(Long professorId, Long groupId) {
        Group group = groupRepository.findByIdFetchProfessors(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));

        group.addProfessor(professor);
    }

    @Transactional
    public void removeProfessorFromGroup(Long professorId, Long groupId) {
        Group group = groupRepository.findByIdFetchProfessors(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        group.removeProfessor(professor);
    }
}
