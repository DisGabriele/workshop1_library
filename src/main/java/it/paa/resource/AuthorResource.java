package it.paa.resource;

import it.paa.model.dto.AuthorDTO;
import it.paa.model.entity.Author;
import it.paa.model.mapper.Mapper;
import it.paa.service.AuthorService;
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

@Path("/authors")
public class AuthorResource {

    @Inject
    AuthorService authorService;

    @Inject
    Validator validator;

    @GET
    public Response getAll(@QueryParam("name") String name, @QueryParam("surname") String surname) {
        try {
            List<Author> authors = authorService.getAll(name, surname);
            return Response.ok(authors)
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(AuthorDTO authorDTO) {
        Author author = Mapper.AuthorMapper(authorDTO);

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(authorService.save(author))
                .build();
    }

    @PUT
    @Path("/id/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, AuthorDTO authorDTO) {

        Author old = authorService.getById(id);
        Author author = Mapper.AuthorMapper(authorDTO);
        author.setId(id);

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        if (!violations.isEmpty()) {

            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        if(!author.oldEquals(old)){
            return Response.ok(
                    authorService.update(author)
            ).build();
        }
        else{
            return Response.status(Response.Status.NOT_MODIFIED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(old)
                    .build();
        }
    }

    @DELETE
    @Path("/id/{id}")
    public Response delete(@PathParam("id") Long id) {
        try{
            authorService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
