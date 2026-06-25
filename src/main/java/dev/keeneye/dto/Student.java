package dev.keeneye.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Student {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fio;
    @Column(name="group_of_students")
    private String group;
    private String phoneNumber;

    public Student(String fio, String group, String phoneNumber) {
        this.fio = fio;
        this.group = group;
        this.phoneNumber = phoneNumber;
    }
}
