package it.paa.resource;

import it.paa.model.dto.GenreDTO;
import it.paa.model.dto.GenreReviewDTO;
import it.paa.model.entity.Genre;
import it.paa.model.mapper.Mapper;
import it.paa.roles.Roles;
import it.paa.service.GenreService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/genres")
@RolesAllowed(Roles.ADMIN)
public class GenreResource {

    @Inject
    GenreService genreService;

    /*
    GET all con possibilità di filtrare per name e description
     */
    @GET
    @Path("/admin")
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
    @Path("/admin/genre_id/{id}")
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

    /*
    GET dell'average reting delle review dei libri associati a questo genere
     */
    @GET
    @Path("/admin/average_rating")
    public Response getAverageRatingById() {
        try {
            List<GenreReviewDTO> genreReviewDTOList = new ArrayList<>();

            genreService.getAll("", "").forEach(genre -> {
                GenreReviewDTO genreReviewDTO = new GenreReviewDTO();
                genreReviewDTO.setGenre(genre);
                genreReviewDTO.setAverageReview(genre.getAverageRating());
                genreReviewDTOList.add(genreReviewDTO);
            });

            return Response.ok(genreReviewDTOList)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
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

    @POST
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(@Valid GenreDTO genreDTO) {

        Genre genre = Mapper.genreMapper(genreDTO);

        return Response.status(Response.Status.CREATED)
                .entity(genreService.save(genre))
                .build();
    }

    @PUT
    @Path("/admin/genre_id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid GenreDTO genreDTO) {
        try {
            Genre old = genreService.getById(id);
            Genre genre = Mapper.genreMapper(genreDTO);
            genre.setId(id);
            genre.setBooks(old.getBooks());

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
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/admin/genre_id/{id}")
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
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("cannot delete this role because there are users associated")
                    .build();
        }
    }
}
