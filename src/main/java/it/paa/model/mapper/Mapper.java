package it.paa.model.mapper;

import io.quarkus.elytron.security.common.BcryptUtil;
import it.paa.model.dto.*;
import it.paa.model.entity.*;

import java.util.List;


public class Mapper {

    public static Genre genreMapper(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genre.setDescription(genreDTO.getDescription());

        return genre;
    }

    public static Author authorMapper(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        author.setBirthDate(authorDTO.getBirthDate());
        author.setNationality(authorDTO.getNationality());

        return author;
    }

    public static Book bookMapper(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setPublishingDate(bookDTO.getPublishingDate());
        book.setPageNumber(bookDTO.getPageNumber());

        return book;
    }

    public static Review reviewMapper(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setText(reviewDTO.getText());
        review.setScore(reviewDTO.getScore());
        review.setDate(reviewDTO.getDate());

        return review;
    }

    public static ReviewAuthorDTO reviewAuthorDTO(Review review) {
        ReviewAuthorDTO reviewAuthorDTO = new ReviewAuthorDTO();
        reviewAuthorDTO.setId(review.getId());
        reviewAuthorDTO.setBook(review.getBook());
        reviewAuthorDTO.setDate(review.getDate());
        reviewAuthorDTO.setScore(review.getScore());
        reviewAuthorDTO.setText(review.getText());
        reviewAuthorDTO.setUser(review.getUser_id());

        return reviewAuthorDTO;
    }

    public static BookAuthorDTO bookAuthorMapper(Book book) {
        BookAuthorDTO bookAuthorDTO = new BookAuthorDTO();
        bookAuthorDTO.setId(book.getId());
        bookAuthorDTO.setTitle(book.getTitle());
        bookAuthorDTO.setAuthor(book.getAuthor());
        bookAuthorDTO.setGenre(book.getGenre());
        bookAuthorDTO.setPublishingDate(book.getPublishingDate());

        List<ReviewAuthorDTO> reviewAuthorDTOList = book.getReviews().stream().map(
                        Mapper::reviewAuthorDTO
                )
                .toList();

        bookAuthorDTO.setReviews(reviewAuthorDTOList);
        bookAuthorDTO.setPageNumber(book.getPageNumber());

        return bookAuthorDTO;
    }

    public static User userMapper(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(BcryptUtil.bcryptHash(userDTO.getPassword()));

        return user;
    }

}
