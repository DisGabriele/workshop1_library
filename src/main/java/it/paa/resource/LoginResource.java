package it.paa.resource;

import io.quarkus.elytron.security.common.BcryptUtil;
import it.paa.model.entity.User;
import it.paa.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/public/login")
public class LoginResource {

    @Inject
    UserService userService;

    @POST
    @PermitAll
    public Response login(@QueryParam("username") String username,@QueryParam("password") String password) {
        if(username == null || password == null || username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Username and password cannot be enpty")
                    .build();
        }
        User user;
        try{
            user = userService.getByName(username);
        }
        catch(NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Username not found")
                    .build();
        }
        if(BcryptUtil.matches(password, user.getPassword())) {
            return Response.ok(username + " has logged in successfully")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        else{
            return Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Password not correct!")
                    .build();
        }

    }
}
