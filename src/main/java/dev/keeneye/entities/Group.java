package dev.keeneye.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "studyGroup", fetch = FetchType.LAZY)
    private List<Student> students;

    @ManyToMany
    @JoinTable(name = "groups_professors",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private List<Professor> professors;


    public void addProfessor(Professor professor) {
        this.professors.add(professor);
        professor.getGroups().add(this);
    }

    public void removeProfessor(Professor professor) {
        this.professors.remove(professor);
        professor.getGroups().remove(this);
    }
}
