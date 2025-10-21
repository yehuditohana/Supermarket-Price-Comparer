package com.example.mystore.config;

import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filter to remove the "Last-Modified" HTTP header from all responses.
 *
 * This helps prevent encoding issues or caching problems,
 * especially when working with Hebrew content.
 */
@Provider
public class StripHebrewHeadersFilter implements ContainerResponseFilter {

    @Override
    public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().remove("Last-Modified");
    }
}