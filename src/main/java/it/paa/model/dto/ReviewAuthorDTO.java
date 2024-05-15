package it.paa.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.paa.model.entity.Book;
import it.paa.model.entity.User;

import java.time.LocalDate;

//DTO usato per l'esercizio 2
public class ReviewAuthorDTO {

    private Long id;

    private String text;

    private Integer score;

    private LocalDate date;

    private User user;

    @JsonBackReference
    private Book book;

    public ReviewAuthorDTO() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
