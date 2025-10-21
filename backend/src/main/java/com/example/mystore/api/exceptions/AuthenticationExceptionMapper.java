package com.example.mystore.api.exceptions;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException exception) {
        return Response.status(Response.Status.UNAUTHORIZED) // 401
                .entity(exception.getMessage())
                .build();
    }
}