package it.paa.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.paa.model.entity.Author;
import it.paa.model.entity.Genre;

import java.util.List;

public class BookAuthorDTO {

    private Long id;

    private String title;

    private Integer publishingDate;

    private Integer pageNumber;

    @JsonManagedReference
    private Genre genre;

    @JsonManagedReference
    private Author author;

    @JsonManagedReference
    private List<ReviewAuthorDTO> reviews;

    public BookAuthorDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Integer publishingDate) {
        this.publishingDate = publishingDate;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<ReviewAuthorDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewAuthorDTO> reviews) {
        this.reviews = reviews;
    }
}
