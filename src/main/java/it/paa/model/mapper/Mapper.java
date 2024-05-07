package it.paa.model.mapper;

import it.paa.model.dto.AuthorDTO;
import it.paa.model.dto.BookDTO;
import it.paa.model.dto.GenreDTO;
import it.paa.model.entity.Author;
import it.paa.model.entity.Book;
import it.paa.model.entity.Genre;


public class Mapper {

    public static Genre GenreMapper(GenreDTO genreDTO){
        Genre genre = new Genre();

        genre.setName(genreDTO.getName());
        genre.setDescription(genreDTO.getDescription());

        return genre;
    }

    public static Author AuthorMapper(AuthorDTO authorDTO){
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        author.setBirthDate(authorDTO.getBirthDate());

        return author;
    }

    public static Book BookMapper(BookDTO bookDTO){
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setPublishingDate(bookDTO.getPublishingDate());
        book.setPageNumber(bookDTO.getPageNumber());

        return book;
    }

}
