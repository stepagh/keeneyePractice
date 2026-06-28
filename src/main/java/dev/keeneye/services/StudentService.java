package dev.keeneye.services;

import dev.keeneye.dto.StudentPage;
import dev.keeneye.entities.Student;
import dev.keeneye.exceptions.InvalidEntityException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(Student student){
        student.setId(null);
        studentRepository.save(student);
        return student;
    }

    public Student updateStudent(Student student) {
        if(student.getId() == null) {
            throw new InvalidEntityException("id of changing student cannot be null");
        }
        studentRepository.save(student);
        return student;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public StudentPage getStudentsPage(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit);
        Slice<Student> students = studentRepository.findAllBy(pageRequest);
        return StudentPage.from(students);
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
        return studentRepository.findByGroup(group);
    }

}
