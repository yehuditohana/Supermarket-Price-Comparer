package com.example.mystore.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * JerseyConfig configures the Jersey framework for handling REST API requests.
 *
 * Responsibilities:
 * - Registers all JAX-RS resources and providers under specified packages.
 * - Manually registers additional filters (e.g., CORSFilter).
 * - Disables WADL generation to avoid exposing unnecessary metadata.
 */


@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {

        packages("com.example.mystore.api", "com.example.mystore.config"); // Scans these packages for @Path and @Provider classes
        register(CORSFilter.class); // Manually register the CORS filter
        property("jersey.config.server.wadl.disableWadl", true); // Disable WADL generation
    }
}