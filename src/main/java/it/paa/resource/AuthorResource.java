package it.paa.resource;

import it.paa.model.dto.AuthorDTO;
import it.paa.model.dto.BookAuthorDTO;
import it.paa.model.entity.Author;
import it.paa.model.mapper.Mapper;
import it.paa.roles.Roles;
import it.paa.service.AuthorService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;

@Path("/authors")
@RolesAllowed(Roles.ADMIN)
public class AuthorResource {

    @Inject
    AuthorService authorService;

    private final Validator validator;

    AuthorResource(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /*
    GET all con possibilità di filtrare per name e surname
     */
    @GET
    @Path("/admin")
    public Response getAll(@QueryParam("name") String name, @QueryParam("surname") String surname) {
        try {
            List<Author> authors = authorService.getAll(name, surname);
            return Response.ok(authors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoContentException e) {
            return Response.noContent()
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/admin/author_id/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            Author author = authorService.getById(id);
            return Response.ok(author)
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
    GET della lista di libri associati ad un autore
     */
    @GET
    @Path("/admin/author_id/{id}/books")
    public Response getBooks(@PathParam("id") Long id) {
        try {
            Author author = authorService.getById(id);

            List<BookAuthorDTO> bookAuthorDTOList = author.getBooks().stream()
                    .map(Mapper::bookAuthorMapper)
                    .toList();


            return Response.ok(bookAuthorDTOList)
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
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(@Valid AuthorDTO authorDTO) {
        Author author = Mapper.authorMapper(authorDTO);

        Set<ConstraintViolation<Author>> validations = validator.validate(author);

        if(!validations.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validations.stream().map(violation ->violation.getPropertyPath()
                            + ": "
                            + violation.getMessage()
                    ))
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(authorService.save(author))
                .build();
    }

    @PUT
    @Path("/admin/author_id/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid AuthorDTO authorDTO) {
        try {
            Author old = authorService.getById(id);
            Author author = Mapper.authorMapper(authorDTO);
            author.setId(id);
            author.setBooks(old.getBooks());

            Set<ConstraintViolation<Author>> validations = validator.validate(author);

            if (!validations.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(validations.stream().map(violation -> violation.getPropertyPath()
                                + ": "
                                + violation.getMessage()
                        ))
                        .build();

            }

            if (!author.oldEquals(old)) {
                return Response.ok(
                        authorService.update(author)
                ).build();
            } else {
                return Response.status(Response.Status.NOT_MODIFIED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(old)
                        .build();
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/admin/author_id/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        try {
            authorService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("cannot delete this role because there are users associated")
                    .build();
        }
    }
}
