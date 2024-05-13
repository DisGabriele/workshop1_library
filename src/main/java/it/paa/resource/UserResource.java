package it.paa.resource;

import it.paa.model.entity.User;
import it.paa.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("users")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Response getAll() {
        try{
            List<User> users = userService.getAll();
            return Response.ok(users)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoContentException e) {
            return Response.noContent()
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /*
    @GET
    @Path("/id/{id}")
    public Response getById(@PathParam("id") Long id) {
        try{
            User user = userService.getById(id);

        }
    }

     */

}
