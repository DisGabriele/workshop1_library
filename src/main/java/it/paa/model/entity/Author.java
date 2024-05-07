package it.paa.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "surname")
    private String surname;


    @Column(name = "birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;


}
