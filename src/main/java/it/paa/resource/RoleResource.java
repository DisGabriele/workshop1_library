package it.paa.resource;

import it.paa.model.entity.Role;
import it.paa.service.RoleService;
import it.paa.util.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/roles")
@RolesAllowed(Roles.ADMIN)
public class RoleResource {

    @Inject
    RoleService roleService;

    @GET
    public Response getAll() {
        try {
            List<Role> roles = roleService.getAll();
            return Response.ok(roles)
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
            Role role = roleService.getById(id);
            return Response.ok(role)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/name/{name}")
    public Response getByName(@PathParam("name") String name) {
        try {
            Role role = roleService.getByName(name);
            return Response.ok(role)
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
    public Response create(@QueryParam("role name") String roleName) {
        try {
            if(roleName == null || roleName.isEmpty() || roleName.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("role cannot be empty")
                        .build();
            }
            Role role = new Role();
            role.setName(roleName);

            return Response.status(Response.Status.CREATED)
                    .entity(roleService.save(role))
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
    public Response update(@PathParam("id") Long id, @QueryParam("role name") String roleName) {
        if(roleName == null || roleName.isEmpty() || roleName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("role cannot be empty")
                    .build();
        }
        Role old = roleService.getById(id);
        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        role.setUsers(old.getUsers());

        try {
            if (!role.oldEquals(old)) {
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

    @DELETE
    @Path("/id/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        try {
            roleService.delete(id);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
