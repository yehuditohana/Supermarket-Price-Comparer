package com.example.mystore.api;


import com.example.mystore.dto.api.request.ComparisonRequestDTO;
import com.example.mystore.dto.api.response.ComparisonResultDTO;
import com.example.mystore.services.apiServices.ItemPriceService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ItemPriceResource handles API requests related to price comparisons of items across different stores.
 * Base path: /itemPrice
 * Consumes: application/json
 * Produces: application/json
 */
@Path("/itemPrice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ItemPriceResource {
    private final ItemPriceService itemPriceService;

    public ItemPriceResource(ItemPriceService itemPriceService) {
        this.itemPriceService = itemPriceService;
    }


    /**
     * Compares the prices of the user's cart across multiple selected stores.
     *
     * The client provides a ComparisonRequestDTO containing:
     * - The user ID
     * - The shopping cart ID
     * - A list of store IDs to compare
     *
     * The server responds with a list of ComparisonResultDTO objects,
     * each containing:
     * - The store information (StoreDTO)
     * - The list of items with their updated prices (ItemWithPriceDTO)
     * - The total price of the cart in that store
     *
     * Important Notes:
     * - If an item is not found in a specific store (foundInStore = false),
     *   its price is not included in the total cart price calculation for that store.
     * - The field 'foundInStore' inside ItemWithPriceDTO indicates whether the item was found.
     *
     * @param request A ComparisonRequestDTO containing the user ID, shopping cart ID, and store IDs for comparison.
     * @return A list of ComparisonResultDTO representing price comparisons across the specified stores.
     */
    @POST
    @Path("/compare")
    public Response compareStores(ComparisonRequestDTO request) {
        List<ComparisonResultDTO> comparisonResultDTOS = itemPriceService.compareCartAcrossStores(request);
        return Response.ok(comparisonResultDTOS).build();
    }


}



