package it.paa.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "publishing_date", nullable = false)
    private Integer publishingDate;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    public Book(){}

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

    public boolean oldEquals(Book book){
        return
                this.title.equals(book.getTitle()) &&
                        this.publishingDate == book.getPublishingDate() &&
                        this.pageNumber == book.getPageNumber();
    }
}
