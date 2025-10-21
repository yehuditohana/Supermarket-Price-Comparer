package com.example.mystore.api;

import com.example.mystore.dto.api.response.StoreDTO;
import com.example.mystore.services.apiServices.StoreService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * StoreResource handles API requests related to retrieving store information,
 * supporting filtering by city, chain, or both.
 *
 * Base path: /stores
 * Produces: application/json
 * Consumes: application/json
 */
@Component
@Path("/stores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StoreResource {

    private final StoreService storeService;

    public StoreResource(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Retrieves a paginated list of all stores.
     *
     * @param page The page number to retrieve (default is 0).
     * @param size The number of stores per page (default is 30).
     * @return A list of StoreDTO representing all stores.
     */
    @GET
    @Path("/all")
    public Response getAllStores(@QueryParam("page") @DefaultValue("0") int page,
                                 @QueryParam("size") @DefaultValue("30") int size) {
        List<StoreDTO> stores = storeService.getAllStores(page, size);
        return Response.ok(stores).build();
    }

    /**
     * Retrieves a paginated list of stores filtered by city name.
     *
     * @param city The name of the city to filter stores by.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of stores per page (default is 30).
     * @return A list of StoreDTO representing stores located in the specified city.
     *         Returns 400 BAD REQUEST if city is missing.
     */
    @GET
    @Path("/by-city")
    public Response getStoresByCity(@QueryParam("city") String city,
                                    @QueryParam("page") @DefaultValue("0") int page,
                                    @QueryParam("size") @DefaultValue("30") int size) {
        if (city == null || city.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required query parameter: city")
                    .build();
        }

        List<StoreDTO> stores = storeService.getStoresByCity(city, page, size);
        return Response.ok(stores).build();
    }


    /**
     * Retrieves a paginated list of stores filtered by chain name.
     *
     * @param chainName The name of the chain to filter stores by.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of stores per page (default is 30).
     * @return A list of StoreDTO representing stores belonging to the specified chain.
     *         Returns 400 BAD REQUEST if chainName is missing.
     */
    @GET
    @Path("/by-chain")
    public Response getStoresByChain(@QueryParam("chainName") String chainName,
                                     @QueryParam("page") @DefaultValue("0") int page,
                                     @QueryParam("size") @DefaultValue("30") int size) {
        if (chainName == null || chainName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required query parameter: chain")
                    .build();
        }

        List<StoreDTO> stores = storeService.getStoresByChain(chainName, page, size);
        return Response.ok(stores).build();
    }

    /**
     * Retrieves a paginated list of stores filtered by both city and chain name.
     *
     * @param city The name of the city.
     * @param chainName The name of the chain.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of stores per page (default is 30).
     * @return A list of StoreDTO representing stores matching the specified city and chain.
     *         Returns 400 BAD REQUEST if either city or chainName is missing.
     */
    @GET
    @Path("/by-city-and-chain")
    public Response getStoresByCityAndChain(@QueryParam("city") String city,
                                            @QueryParam("chainName") String chainName,
                                            @QueryParam("page") @DefaultValue("0") int page,
                                            @QueryParam("size") @DefaultValue("30") int size) {
        if ((city == null || city.isBlank()) || (chainName == null || chainName.isBlank())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("City and chain name are required.")
                    .build();
        }

        List<StoreDTO> stores = storeService.getStoresByCityAndChain(city, chainName, page, size);
        return Response.ok(stores).build();
    }

}



