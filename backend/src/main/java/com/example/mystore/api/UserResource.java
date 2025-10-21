package com.example.mystore.api;

import com.example.mystore.services.apiServices.UserService;
import com.example.mystore.dto.api.request.RegisterDTO;
import com.example.mystore.dto.api.response.UserSummaryDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * UserResource handles API requests related to user registration, login, and logout.
 *
 * Base path: /users
 * Produces: application/json
 * Consumes: application/json
 */

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class UserResource {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user in the system.
     *
     * @param dto A RegisterDTO containing user registration information.
     * @return A UserSummaryDTO representing the newly registered user.
     *         Returns 201 CREATED upon successful registration.
     */
@POST
@Path("/register")
public Response registerUser(RegisterDTO dto) {
    UserSummaryDTO userSummary = userService.registerUser(dto);
    return Response.status(Response.Status.CREATED)
            .entity(userSummary)
            .build();
}

    /**
     * Authenticates an existing user by email and password.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return A UserSummaryDTO containing user information if authentication is successful.
     *         Returns 200 OK upon successful login.
     *         Returns an error if authentication fails (error handling done inside the service layer).
     */
    @POST
    @Path("/login")
    public Response loginUser(@QueryParam("email") String email, @QueryParam("password") String password) {
   //Check if there is a registered user with these details
        UserSummaryDTO userSummaryDTO = userService.authenticateUser(email, password);
     //Return user information if login was successful
            return Response.ok(userSummaryDTO).build();
            //Return error if there is a problem with authentication.

    }

    /**
     * Logs out a user based on their session number.
     *
     * @param sessionNumber The session identifier of the logged-in user.
     * @return A message confirming successful logout.
     */
    @POST
    @Path("/logout")
    public Response logoutUser(@QueryParam("session") String sessionNumber) {
        System.out.println("📥 Session received: " + sessionNumber);
        userService.logoutUser(sessionNumber);
        return Response.ok(Map.of("message", "Logout successful")).build();
    }


}