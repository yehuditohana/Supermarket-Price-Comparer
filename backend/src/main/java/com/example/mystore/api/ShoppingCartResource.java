package com.example.mystore.api;

import com.example.mystore.dto.api.response.CartDto;
import com.example.mystore.dto.api.response.UserSummaryDTO;
import com.example.mystore.services.apiServices.ShoppingCartService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ShoppingCartResource handles API requests related to shopping cart management,
 * including creation, activation, archiving, deletion, and history retrieval.
 *
 * Base path: /shopping-carts
 * Produces: application/json
 * Consumes: application/json
 */
@Path("/shopping-carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class ShoppingCartResource {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartResource(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * Retrieves the ID of the user's active shopping cart.
     *
     * If no active cart exists, a new active cart will be automatically created.
     *
     * @param userId The ID of the user.
     * @return The ID of the active shopping cart.
     */
    @GET
    @Path("/active/{userId}")
    public Response getActiveCartId(@PathParam("userId") Long userId) {
        Long cartId = shoppingCartService.getActiveCartForUser(userId);
        return Response.ok(cartId).build();
    }

    /**
     * Archives a shopping cart by setting its status to archived and assigning it a name.
     *
     * @param cartId The ID of the cart to archive.
     * @param cartName The name to assign to the archived cart.
     * @return A confirmation message upon successful archiving.
     */
    @PUT
    @Path("/archive")
    public Response archiveCart(
            @QueryParam("cartId") Long cartId,
            @QueryParam("cartName") String cartName) {
        System.out.println("🛒 cartId = " + cartId);
        System.out.println("📝 cartName = " + cartName);
        shoppingCartService.archiveCart(cartId, cartName);
        return Response.ok("\"archived\"").build();
    }


    /**
     * Deletes a shopping cart along with all its associated items.
     *
     * @param cartId The ID of the cart to delete.
     * @return A 204 No Content response if deletion is successful.
     */
    @DELETE
    @Path("/{cartId}")
    public Response deleteShoppingCart(@PathParam("cartId") Long cartId) {
        shoppingCartService.deleteCart(cartId);
        return Response.noContent().build();
    }

    /**
     * Retrieves a list of all archived shopping carts for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of CartDto representing the user's archived carts.
     */    @GET
    @Path("/history/{userId}")
    public Response getCartHistory(@PathParam("userId") Long userId) {
        List<CartDto> carts = shoppingCartService.getArchivedCartsByUser(userId);
        return Response.ok(carts).build();
    }

    /**
     * Reactivates a previously archived shopping cart by creating a new active cart
     * based on the archived cart's contents.
     *
     * @param cartId The ID of the archived cart to activate.
     * @return A 204 No Content response if activation is successful.
     */
    @PUT
    @Path("/{cartId}/activate")
    public Response activateShoppingCart(@PathParam("cartId") Long cartId) {
        shoppingCartService.activateCart(cartId);
        return Response.noContent().build();
    }



}