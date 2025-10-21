package com.example.mystore.api;

import com.example.mystore.dto.api.response.CartItemDTO;
import com.example.mystore.services.apiServices.CartItemsService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * CartItemsResource handles API requests related to managing items within a shopping cart.
 * Base path: /cart-items
 * Consumes: application/json
 * Produces: application/json
 */
@Path("/cart-items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class CartItemsResource {
    private final CartItemsService cartItemsService;

    public CartItemsResource(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    /**
     * Retrieves the list of items from the active shopping cart associated with a specific user.
     * @param userId The ID of the user whose active cart items are requested.
     * @return A list of CartItemDTO representing the items in the user's active cart.
     */
    @GET
    @Path("/user/{userId}")
    public Response getCartItemsByUserId(@PathParam("userId") Long userId) {
        List<CartItemDTO> items = cartItemsService.getItemsFromActiveCart(userId);
        return Response.ok(items).build();
    }
    /**
     * Retrieves the list of items contained within a specific cart by cart ID.
     *
     * @param cartId The ID of the cart whose items are requested.
     * @return A list of CartItemDTO representing the items in the specified cart.
     */
    @GET
    @Path("/cart/{cartId}")
    public Response getCartItemsByCartId(@PathParam("cartId") Long cartId) {
        List<CartItemDTO> items = cartItemsService.getItemsByCartId(cartId);
        return Response.ok(items).build();
    }

    /**
     * Adds an item to the cart or updates its quantity if it already exists.
     * @param cartId   The ID of the cart to which the item should be added.
     * @param itemId   The ID of the item to add.
     * @param quantity The quantity of the item to add or update.
     * @return A 204 No Content response if the operation is successful.
     */
    @POST
    @Path("/{cartId}/items/{itemId}")
    public Response addItemToCart(@PathParam("cartId") Long cartId,
                                  @PathParam("itemId") String itemId,
                                  @QueryParam("quantity") int quantity) {
        cartItemsService.addOrUpdateItemInCart(cartId, itemId, quantity);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Removes a specified quantity of an item from the cart.
     * If the quantity reaches zero, the item is removed entirely.
     *
     * @param cartId   The ID of the cart from which the item should be removed.
     * @param itemId   The ID of the item to remove.
     * @param quantity The quantity of the item to remove.
     * @return A 204 No Content response if the operation is successful.
     */
    @DELETE
    @Path("/{cartId}/items/{itemId}")
    public Response removeItemFromCart(@PathParam("cartId") Long cartId,
                                       @PathParam("itemId") String itemId,
                                       @QueryParam("quantity") int quantity) {
        cartItemsService.removeItemFromCart(cartId, itemId, quantity);
        return Response.noContent().build();
    }

}