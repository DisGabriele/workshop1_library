package it.paa.resource;

import it.paa.model.dto.GenreDTO;
import it.paa.model.entity.Genre;
import it.paa.model.mapper.Mapper;
import it.paa.service.GenreService;
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

@Path("/genres")
public class GenreResource {

    @Inject
    GenreService genreService;

    @Inject
    Validator validator;

    @GET
    public Response getAll(@QueryParam("name") String name, @QueryParam("description") String description) {
        try {
            List<Genre> genres = genreService.getAll(name, description);
            return Response.ok(genres)
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
            Genre genre = genreService.getById(id);
            return Response.ok(genre)
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
    public Response create(GenreDTO genreDTO) {

        Set<ConstraintViolation<GenreDTO>> violations = validator.validate(genreDTO);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        Genre genre = Mapper.GenreMapper(genreDTO);

        return Response.status(Response.Status.CREATED)
                .entity(genreService.save(genre))
                .build();
    }

    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, GenreDTO genreDTO) {

        Set<ConstraintViolation<GenreDTO>> violations = validator.validate(genreDTO);

        if (!violations.isEmpty()) {

            String errorMessage = violations.stream()
                    .map(violation -> String.format("%s", violation.getMessage()))
                    .collect(Collectors.joining("\n"));

            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(errorMessage)
                    .build();
        }

        Genre old = genreService.getById(id);
        Genre genre = Mapper.GenreMapper(genreDTO);
        genre.setId(id);

        if (!genre.oldEquals(old)) {
            return Response.ok(
                    genreService.update(genre)
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
            genreService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }
}