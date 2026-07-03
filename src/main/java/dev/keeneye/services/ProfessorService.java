package dev.keeneye.services;

import dev.keeneye.dto.*;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Professor;
import dev.keeneye.entities.Student;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.mappers.ProfessorMapper;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.repositories.ProfessorRepository;
import dev.keeneye.repositories.StudentRepository;
import dev.keeneye.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final ProfessorMapper professorMapper;



    @Transactional
    public StudentsPage getProfessorsStudents(Long professorId, int limit, int offset) {

        PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.by("id").ascending());
        Slice<Student> students = studentRepository.findAllStudentsByProfessorId(professorId, pageRequest);
        return StudentsPage.from(students);
    }

    public ProfessorsPage getAllProfessorsPage(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.by("id").ascending());
        Slice<ProfessorResponse> professorSlice = professorRepository.findAllBy(pageRequest).map(professorMapper::toResponse);
        return ProfessorsPage.from(professorSlice);
    }

    public ProfessorResponse getProfessorById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id " + id + " not found"));
        return professorMapper.toResponse(professor);
    }


    public ProfessorResponse updateProfessor(Long id, ProfessorRequest request) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor with id: " + id + " not found"));
        professor.setFio(request.fio());
        professor.setPhoneNumber(request.phoneNumber());
        professorRepository.save(professor);
        return professorMapper.toResponse(professor);
    }

    @Transactional
    public void deleteProfessor(Long id) {
        Professor professor = professorRepository.findByidFetchGroupsProfessors(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor with id: " + id + " not found"));

        for (Group group : professor.getGroups()) {
            group.removeProfessor(professor);
        }
        professorRepository.delete(professor);
    }

}
