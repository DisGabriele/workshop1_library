package it.paa.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

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

    @ManyToOne
    @JoinColumn(name = "genre", referencedColumnName = "id")
    @JsonManagedReference
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    @JsonManagedReference
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    /*
    per non far visualizzare la lista quando si va a fare una get
    per l'esercizio 2, ho utilizzato 2 dto (ReviewAuthorDTO,BookAuthorDTO)con la reference inversa (back reference di book nella Review e managed reference nella lista Review di Book)
     */
    private List<Review> reviews;

    public Book() {
    }

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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    @JsonIgnore
    public Double getAverageRating() {
        System.out.println(getReviews().size());
        OptionalDouble averageRating = getReviews().stream()
                .mapToInt(review ->{
                    System.out.println(review.getScore());
                    return review.getScore();
                } )
                .average();

        return averageRating.orElse(0.0);
    }

    public boolean oldEquals(Book book) {
        return
                this.title.equals(book.getTitle()) &&
                        this.publishingDate.equals(book.getPublishingDate()) &&
                        this.pageNumber.equals(book.getPageNumber()) &&
                        this.author.equals(book.getAuthor()) &&
                        this.genre.equals(book.getGenre());
    }
}
