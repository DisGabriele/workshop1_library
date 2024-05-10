package it.paa.resource;

import it.paa.model.dto.BookDTO;
import it.paa.model.entity.Author;
import it.paa.model.entity.Book;
import it.paa.model.entity.Genre;
import it.paa.model.mapper.Mapper;
import it.paa.service.AuthorService;
import it.paa.service.BookService;
import it.paa.service.GenreService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/books")
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    GenreService genreService;

    @Inject
    AuthorService authorService;

    /*
    GET all con possibilità di filtrare per titolo e intervallo di tempo date 2 date
     */
    @GET
    public Response getAll(@QueryParam("title") String title, @QueryParam("start date") Integer startDate, @QueryParam("end date") Integer endDate) {
        try {
            List<Book> books = bookService.getAll(title, startDate, endDate);
            return Response.ok(books)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        } catch (NoContentException e) {
            return Response.noContent()
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/id/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            Book book = bookService.getById(id);
            return Response.ok(book)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /*
    GET delle review di un libro
     */
    @GET
    @Path("/id/{id}/reviews")
    public Response getReviews(@PathParam("id") Long id) {
        try{
            Book book = bookService.getById(id);

            return Response.ok(book.getReviews())
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        }
        catch(NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(@Valid BookDTO bookDTO) {
        Author author;
        try {
            author = authorService.getById(bookDTO.getAuthorId());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        Genre genre;
        try {
            genre = genreService.getById(bookDTO.getGenreId());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        Book book = Mapper.bookMapper(bookDTO);
        book.setAuthor(author);
        book.setGenre(genre);

        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity(bookService.save(book))
                .build();
    }

    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid BookDTO bookDTO) {
        Book old = bookService.getById(id);
        Book book = Mapper.bookMapper(bookDTO);
        book.setId(old.getId());
        book.setReviews(old.getReviews());

        Author author;
        try {
            author = authorService.getById(bookDTO.getAuthorId());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        Genre genre;
        try {
            genre = genreService.getById(bookDTO.getGenreId());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        book.setAuthor(author);
        book.setGenre(genre);

        if (!book.oldEquals(old)) {
            return Response.ok(
                    bookService.update(book)
            ).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(old)
                    .build();
        }
    }

    @DELETE
    @Path("/id/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        try {
            bookService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
