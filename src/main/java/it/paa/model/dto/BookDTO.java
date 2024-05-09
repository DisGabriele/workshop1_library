package it.paa.model.dto;

import it.paa.model.entity.validation.book.PastYearConstraint;
import jakarta.validation.constraints.*;

//usato per la POST e la GET per non far inserire manualmente id e lista Review
public class BookDTO {
    @NotBlank(message = "title cannot be empty")
    private String title;

    @NotNull(message = "publishing date must not be null")
    @PastYearConstraint
    private Integer publishingDate;

    @NotNull(message = "page number must not be null")
    @Min(value = 1, message = "page number must be at least 1")
    @Max(value = 5000, message = "page number must be at most 5000")
    private Integer pageNumber;

    @NotNull(message = "genre must not be null")
    private long genreId;

    @NotNull(message = "author must not be null")
    private long authorId;

    public BookDTO(){}

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

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
