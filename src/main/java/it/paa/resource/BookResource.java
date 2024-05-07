package it.paa.resource;

import it.paa.model.dto.BookDTO;
import it.paa.model.entity.Book;
import it.paa.model.mapper.Mapper;
import it.paa.service.BookService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/books")
public class BookResource {

    @Inject
    BookService bookService;

    @Inject
    Validator validator;

    @GET
    public Response getAll(@QueryParam("title") String title, @QueryParam("start date") Integer startDate, @QueryParam("end date") Integer endDate) {
        try {
            List<Book> books = bookService.getAll(title, startDate, endDate);
            return Response.ok(books)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(BookDTO bookDTO) {

        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        Book book = Mapper.BookMapper(bookDTO);

        return Response.status(Response.Status.CREATED)
                .entity(bookService.save(book))
                .build();
    }

    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, BookDTO bookDTO) {

        Set<ConstraintViolation<BookDTO>> violations = validator.validate(bookDTO);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        Book old = bookService.getById(id);
        Book book = Mapper.BookMapper(bookDTO);
        book.setId(old.getId());

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
