package dev.keeneye.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User user;
    private String fio;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group studyGroup;

    public Student(String fio, Group group, String phoneNumber) {
        this.fio = fio;
        this.studyGroup = group;
        this.phoneNumber = phoneNumber;
    }
}