package br.com.api.studentscontrol.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "STUDENT")
public class StudentModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, name = "NAME")
    private String name;

    @Column(nullable = false, name = "COURSE")
    private String course;

    @Column(nullable = false, name = "BIRTH")
    private LocalDateTime birth;
}
