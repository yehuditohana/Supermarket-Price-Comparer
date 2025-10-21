package com.example.mystore.api;

import com.example.mystore.services.apiServices.ItemService;
import com.example.mystore.dto.api.response.ItemDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * ItemResource handles API requests related to retrieving and searching for items.
 *
 * Base path: /item
 * Produces: application/json
 * Consumes: application/json
 */
@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ItemResource {
    private final ItemService itemService;

    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Retrieves a paginated list of items by general category name.
     *
     * @param category The name of the general category.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of items per page (default is 10).
     * @return A list of ItemDTO representing the items in the specified general category.
     */
    @GET
    @Path("/by-general-category")
    public Response getItemsByGeneralCategory(
            @QueryParam("category") String category,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size) {

        List<ItemDTO> items = itemService.getItemsByCategory("General", category, page, size);
        return Response.ok(items).build();
    }

    /**
     * Retrieves a paginated list of items by subcategory name.
     *
     * @param category The name of the subcategory.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of items per page (default is 10).
     * @return A list of ItemDTO representing the items in the specified subcategory.
     */
    @GET
    @Path("/by-sub-category")
    public Response getItemsBySubCategory(
            @QueryParam("category") String category,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size) {

        List<ItemDTO> items = itemService.getItemsByCategory("Sub",category, page, size);
        return Response.ok(items).build();
    }

    /**
     * Retrieves a paginated list of items by specific category name.
     *
     * @param category The name of the specific category.
     * @param page The page number to retrieve (default is 0).
     * @param size The number of items per page (default is 10).
     * @return A list of ItemDTO representing the items in the specified specific category.
     */
    @GET
    @Path("/by-specific-category")
    public Response getItemsBySpecificCategory(
            @QueryParam("category") String category,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size) {

        List<ItemDTO> items = itemService.getItemsByCategory("Specific", category, page, size);
        return Response.ok(items).build();
    }


/**
     * Searches for items by name or item ID.
     * Returns paginated results based on the query.
     *
     * @param query The search keyword (can be item name or item ID).
     * @param page The page number to retrieve (default is 0).
     * @param size The number of items per page (default is 10).
     * @return A Page of ItemDTO matching the search query.
     */
    @GET
    @Path("/search")
    public Response searchItem(
            @QueryParam("query") String query,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size)  {
        Page<ItemDTO> items = itemService.searchItem(query , page , size);
        return Response.ok(items).build();
    }

    /**
     * Retrieves a list of alternative items from the same specific category,
     * based on a given item and store.
     *
     * Only alternatives that are available (exist) in the specified store are returned.
     *
     * Important:
     * - Although ItemDTO is generally used for representing products without store-specific pricing,
     *   in this endpoint, the fields 'lowestPrice' and 'highestPrice' both refer to the actual price
     *   of the item in the specified store (by store id).
     * - Therefore, for alternatives, 'lowestPrice' and 'highestPrice' will contain the real price of the item,
     *   rather than representing a general price range.
     *
     * @param storeId The ID of the store to search alternatives in.
     * @param itemId The ID of the original item to find alternatives for.
     * @return A list of ItemDTO representing alternative items available in the specified store,
     *         each including its actual price.
     */
    @GET
    @Path("/alternatives")
    public Response getItemsBySpecificCategory(
            @QueryParam("storeId") Long storeId,
            @QueryParam("itemId") String itemId) {
        List<ItemDTO> items = itemService.findAlternatives(storeId , itemId);
        return Response.ok(items).build();
    }


}