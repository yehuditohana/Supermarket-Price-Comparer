package com.example.mystore.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CORSFilter adds Cross-Origin Resource Sharing (CORS) headers to all HTTP responses.
 *
 * Allows the frontend application (possibly running on a different domain or port) to access the backend APIs.
 * This filter permits all origins and common HTTP methods.
 */

@Component
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        System.out.println("CORSFilter activated for: " + requestContext.getUriInfo().getRequestUri());

        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}

