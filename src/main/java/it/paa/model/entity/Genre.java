package it.paa.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "genres")
//@UniqueConstraintGenre
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    public Genre(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean oldEquals(Genre genre) {
        return
                this.name.equals(genre.getName()) &&
                        this.description.equals(genre.getDescription());
    }
}
