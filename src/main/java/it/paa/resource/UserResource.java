package it.paa.resource;

import it.paa.model.dto.UserDTO;
import it.paa.model.entity.Role;
import it.paa.model.entity.User;
import it.paa.model.mapper.Mapper;
import it.paa.service.RoleService;
import it.paa.service.UserService;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("users")
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @GET
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
    @Path("/id/{id}")
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
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, @Valid UserDTO userDTO) {
        Role role;
        try {
            role = roleService.getByName(userDTO.getRole());
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }

        User old = userService.getById(id);
        User user = Mapper.userMapper(userDTO);
        old.setId(id);
        old.setRole(role);

        try {
            if (!user.oldEquals(old)) {
                return Response.ok(
                        roleService.update(role)
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
    }
}
