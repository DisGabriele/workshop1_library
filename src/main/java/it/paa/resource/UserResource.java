package it.paa.resource;

import io.quarkus.elytron.security.common.BcryptUtil;
import it.paa.model.dto.UserDTO;
import it.paa.model.entity.Role;
import it.paa.model.entity.User;
import it.paa.model.mapper.Mapper;
import it.paa.roles.Roles;
import it.paa.service.RoleService;
import it.paa.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@RolesAllowed(Roles.ADMIN)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @GET
    @Path("/admin")
    public Response getAll() {
        try {
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


    @GET
    @Path("/admin/user_id/{id}")
    public Response getById(@PathParam("id") Long id) {
        try {
            User user = userService.getById(id);

            return Response.ok(user)
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
    public Response create(@Valid UserDTO userDTO) {
        Role role;
        try {
            role = roleService.getByName(userDTO.getRole());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        User user = Mapper.userMapper(userDTO);
        user.setRole(role);

        try {
            return Response.status(Response.Status.CREATED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(userService.save(user))
                    .build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/admin/user_id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid UserDTO userDTO) {
        try {
            User old = userService.getById(id);

            Role role;
            try {
                role = roleService.getByName(userDTO.getRole());
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND)
                        .type(MediaType.TEXT_PLAIN)
                        .entity(e.getMessage())
                        .build();
            }

            User user = Mapper.userMapper(userDTO);
            user.setId(id);
            user.setRole(role);
            user.setReviews(old.getReviews());

            try {
                if (!(user.getUsername().equals(old.getUsername())) &&
                        BcryptUtil.matches(userDTO.getPassword(), old.getPassword()) &&
                        user.getRole().equals(old.getRole())
                ) {
                    return Response.ok(
                            userService.update(user)
                    ).build();
                } else {
                    return Response.status(Response.Status.NOT_MODIFIED)
                            .type(MediaType.APPLICATION_JSON)
                            .entity(old)
                            .build();
                }
            } catch (PersistenceException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN)
                        .entity(e.getMessage())
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
    @Path("/admin/user_id/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        try {
            userService.delete(id);

            return Response.ok()
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
