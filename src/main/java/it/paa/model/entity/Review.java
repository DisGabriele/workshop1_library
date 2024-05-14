package it.paa.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"book","user_id"}))
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", columnDefinition = "text", nullable = false)
    private String text;

    @Column(name = "score")
    private Integer score;

    @Column(name = "date")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "book", referencedColumnName = "id")
    @JsonManagedReference
    /*
    per far visualizzare il Book nella get
    per l'esercizio 2, ho utilizzato 2 dto (ReviewAuthorDTO,BookAuthorDTO)con la reference inversa (back reference di book nella Review e managed reference nella lista Review di Book)
    */
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    private User user_id;

    public Review() {
    }

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

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user) {
        this.user_id = user;
    }

    public boolean oldEquals(Review review) {
        return
                this.text.equals(review.text) &&
                        this.score.equals(review.score) &&
                        this.date.equals(review.date);
    }
}
