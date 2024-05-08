package it.paa.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", columnDefinition = "text", nullable = false)
    private String text;

    @Column(name = "score")
    private Integer score;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "book", referencedColumnName = "id")
    @JsonManagedReference
    private Book book;

    public Review(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean oldEquals(Review review) {
        return
                this.text.equals(review.text) &&
                        this.score.equals(review.score) &&
                        this.date.equals(review.date);
    }
}
