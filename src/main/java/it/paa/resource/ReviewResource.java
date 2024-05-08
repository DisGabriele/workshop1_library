package it.paa.resource;

import it.paa.model.dto.ReviewDTO;
import it.paa.model.entity.Book;
import it.paa.model.entity.Review;
import it.paa.model.mapper.Mapper;
import it.paa.service.BookService;
import it.paa.service.ReviewService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/reviews")
public class ReviewResource {

    @Inject
    ReviewService reviewService;

    @Inject
    BookService bookService;

    @GET
    public Response getAll(@QueryParam("score") Integer score, @QueryParam("start date") String startDateString, @QueryParam("end date") String endDateString) {
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;

            if (startDateString != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    startDate = LocalDate.parse(startDateString, formatter);
                } catch (DateTimeParseException e) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        startDate = LocalDate.parse(startDateString, formatter);

                    } catch (DateTimeParseException ex) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .type(MediaType.TEXT_PLAIN)
                                .entity("Invalid date format")
                                .build();
                    }
                }
            }

            //check per fare in modo che la data può essere messa in entrambi i modi
            if (endDateString != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    endDate = LocalDate.parse(endDateString, formatter);
                } catch (DateTimeParseException e) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        endDate = LocalDate.parse(endDateString, formatter);
                    } catch (DateTimeParseException ex) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .type(MediaType.TEXT_PLAIN)
                                .entity("Invalid date format")
                                .build();
                    }
                }
            }

            List<Review> review = reviewService.getAll(score, startDate, endDate);
            return Response.ok(review)
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
            Review review = reviewService.getById(id);
            return Response.ok(review)
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
    @Path("/book_id/{book_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(@PathParam("book_id") Long bookId, @Valid ReviewDTO reviewDTO) {
        Book book;
        try {
            book = bookService.getById(bookId);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        Review review = Mapper.reviewMapper(reviewDTO);
        review.setBook(book);

        return Response.status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .entity(reviewService.save(review))
                .build();
    }

    @PUT
    @Path("/id/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid ReviewDTO reviewDTO) {
        Review old = reviewService.getById(id);
        Review review = Mapper.reviewMapper(reviewDTO);
        review.setId(old.getId());

        if (!review.oldEquals(old)) {
            return Response.ok(
                    reviewService.update(review)
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
            reviewService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
