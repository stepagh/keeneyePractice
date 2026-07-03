package dev.keeneye.services;

import dev.keeneye.dto.StudentsPage;
import dev.keeneye.dto.StudentRequest;
import dev.keeneye.dto.StudentResponse;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Student;
import dev.keeneye.entities.User;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.mappers.StudentMapper;
import dev.keeneye.repositories.GroupRepository;
import dev.keeneye.repositories.StudentRepository;
import dev.keeneye.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public Student createStudent(Student student){
        student.setId(null);
        studentRepository.save(student);
        return student;
    }

    @Transactional
    public Student updateProfile(StudentRequest request, Long studentId, Long userId) {
        User user = userRepository.findById(userId).get();
        if (!user.getStudent().getId().equals(studentId))
            throw new AccessDeniedException("Student tries to change not his profile");

        Group group = groupRepository.findByName(request.groupName())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Student student = studentMapper.toEntity(request, studentId, group);
        studentRepository.save(student);
        return student;
    }

    @Transactional
    public List<StudentResponse> getClassmates(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<StudentResponse> classmates = student.getStudyGroup().getStudents().stream().map(studentMapper::toResponse).toList();
        return classmates;
    }

    public StudentResponse changeStudentById(Long studentId, StudentRequest studentRequest) {
        Group group = groupRepository.findByName(studentRequest.groupName())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        Student student = studentMapper.toEntity(studentRequest, studentId, group);
        studentRepository.save(student);
        return studentMapper.toResponse(student);
    }


    public StudentsPage getStudentPage(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.by("id").ascending());
        Slice<Student> studentSlice = studentRepository.findAllBy(pageRequest);
        return StudentsPage.from(studentSlice);
    }


    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        studentRepository.deleteById(id);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    public List<Student> getStudentsByGroup(String group) {
        return studentRepository.findByStudyGroup_Name(group);
    }

}
