package it.paa.resource;

import it.paa.model.dto.ReviewDTO;
import it.paa.model.entity.Book;
import it.paa.model.entity.Review;
import it.paa.model.entity.User;
import it.paa.model.mapper.Mapper;
import it.paa.roles.Roles;
import it.paa.service.BookService;
import it.paa.service.ReviewService;
import it.paa.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

//ho diviso i metodi per le varie autorizzazioni

@Path("/reviews")
public class ReviewResource {

    @Inject
    ReviewService reviewService;

    @Inject
    BookService bookService;

    @Inject
    UserService userService;

    @Context
    SecurityContext securityContext;

    private final Validator validator;

    ReviewResource() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /*
    GET all con possibilità di filtrare per score e intervallo di tempo date 2 date
     */
    @GET
    @Path("/auth")
    @RolesAllowed({Roles.ADMIN, Roles.USER}) //admin può vedere tutte, user vedrà solo le sue
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
            //passo l'username per distinguere la chiamata fatta dall'admin da quella fatta dall'user (admin vede tutte le review, user solo le sue)
            String username = "";
            if (securityContext.isUserInRole(Roles.USER))
                username = securityContext.getUserPrincipal().getName();

            List<Review> review = reviewService.getAll(score, startDate, endDate, username);
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
    @Path("/admin/review_id/{id}")
    @RolesAllowed({Roles.ADMIN}) //ricerca per review_id
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

    /*
    POST di una review dato un libro
     */
    @POST
    @Path("/user/book_id/{book_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed(Roles.USER)
    public Response create(@PathParam("book_id") Long bookId, @Valid ReviewDTO reviewDTO) {
        User user;
        try {
            user = userService.getByName(securityContext.getUserPrincipal().getName());
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

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
        review.setUser_id(user);

        Set<ConstraintViolation<Review>> validations = validator.validate(review);

        if (!validations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validations)
                    .build();
        }

        try {
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(reviewService.save(review))
                    .build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/auth/review_id/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    @RolesAllowed({Roles.ADMIN,Roles.USER}) //admin può modificarle tutte, user solo le sue
    public Response update(@PathParam("id") Long id, @Valid ReviewDTO reviewDTO) {
        try {
            Review old = reviewService.getById(id);

            if(securityContext.isUserInRole(Roles.USER)){

            String username;
            username = securityContext.getUserPrincipal().getName();

            if (!old.getUser_id().getUsername().equals(username))
                return Response.status(Response.Status.FORBIDDEN)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("cannot edit review written by other users")
                        .build();

            }
            Review review = Mapper.reviewMapper(reviewDTO);
            review.setId(old.getId());
            review.setBook(old.getBook());
            review.setUser_id(old.getUser_id());

            Set<ConstraintViolation<Review>> validations = validator.validate(review);

            if (!validations.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(validations)
                        .build();
            }

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
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/user/book_id/{book_id}")
    @Consumes({MediaType.APPLICATION_JSON}) //modifica review di un utente dato il libro
    @Transactional
    @RolesAllowed({Roles.USER})
    public Response updateByBookId(@PathParam("book_id") Long bookId, @Valid ReviewDTO reviewDTO) {
        String username;
        username = securityContext.getUserPrincipal().getName();

        try {
            Review old = reviewService.getByBookIdAndUser(bookId, username);

            if (securityContext.isUserInRole(Roles.USER) && !old.getUser_id().getUsername().equals(username))
                return Response.status(Response.Status.FORBIDDEN)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("cannot edit review written by other users")
                        .build();

            Review review = Mapper.reviewMapper(reviewDTO);
            review.setId(old.getId());
            review.setBook(old.getBook());
            review.setUser_id(old.getUser_id());

            Set<ConstraintViolation<Review>> validations = validator.validate(review);

            if (!validations.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(validations)
                        .build();
            }

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
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(username + " did not written a review for book with id " + bookId)
                    .build();
        }
    }

    @DELETE
    @Path("/auth/review_id/{id}")
    @Transactional
    @RolesAllowed({Roles.ADMIN, Roles.USER})
    //lo lascio a tutti visto che un admin può rimuovere una recensione (come se fosse un moderatore)
    public Response delete(@PathParam("id") Long id) {
        try {
            Review review = reviewService.getById(id);

            if (securityContext.isUserInRole(Roles.USER)) {
                String username;
                username = securityContext.getUserPrincipal().getName();

                if (!review.getUser_id().getUsername().equals(username))
                    return Response.status(Response.Status.FORBIDDEN)
                            .type(MediaType.TEXT_PLAIN)
                            .entity("a user cannot delete review written by other users")
                            .build();
            }

            reviewService.delete(review);

            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/auth/book_id/{book_id}")
    @Transactional
    @RolesAllowed(Roles.USER) //eliminare recensione dell'utente dato l'id di un libro (solo user esistono piu' recensioni per un libro)
    public Response deleteByBookId(@PathParam("book_id") Long bookId) {
            String username;
            username = securityContext.getUserPrincipal().getName();

            User user = userService.getByName(username);

            Review review = user.getReviews().stream().filter(review1 -> review1.getBook().getId().equals(bookId))
                    .findFirst().orElse(null);

            if(review != null){
                user.getReviews().remove(review);
                userService.update(user);
                return Response.ok().build();
            }
            else
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity(username + " did not write a review for book with id " + bookId)
                        .build();
    }
}
