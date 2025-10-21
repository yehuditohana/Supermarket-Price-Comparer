package com.example.mystore.services.apiServices;

import com.example.mystore.database.entities.CartItemKey;
import com.example.mystore.database.entities.CartItem;
import com.example.mystore.database.entities.Item;
import com.example.mystore.database.entities.ShoppingCart;
import com.example.mystore.database.repositories.CartItemRepository;
import com.example.mystore.database.repositories.ShoppingCartRepository;
import com.example.mystore.dto.api.response.CartItemDTO;
import com.example.mystore.services.CartStatus;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemsService {
    private final CartItemRepository cartItemsRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public CartItemsService(CartItemRepository cartItemsRepository, ShoppingCartRepository shoppingCartRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<CartItemDTO> getItemsFromActiveCart(Long userId) {
        Optional<ShoppingCart> optActiveCart = shoppingCartRepository.findByUser_UserIDAndStatus(userId, CartStatus.ACTIVE);

        if (optActiveCart.isEmpty()) {
            throw new EntityNotFoundException("No active cart found for user " + userId);
        }

        ShoppingCart activeCart = optActiveCart.get();
        List<CartItem> cartItems = cartItemsRepository.findById_CartID(activeCart.getCartID());

        return cartItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public void addOrUpdateItemInCart(Long cartId, String itemId, int quantity) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(cartId);
        // Check if the shopping cart exists
        if(optionalShoppingCart.isEmpty()){
            throw new EntityNotFoundException("Cart with ID " + cartId + " not found");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        //create key for cart item table
        CartItemKey cartItemKey = new CartItemKey(shoppingCart.getCartID(), itemId);

        // Check if the item already exists in the cart (based on (cart_id, item_id) key)
        Optional<CartItem> existingCartItem = cartItemsRepository.findById(cartItemKey);

        if (existingCartItem.isPresent()) {
            //recorde exists , update quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemsRepository.save(cartItem);
        //new item cart - create new record in "CartItem" table
        } else {
            //recorde not exists ,crate new.
            CartItem newCartItem = new CartItem();
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setId(cartItemKey);
            newCartItem.setQuantity(quantity);
            cartItemsRepository.save(newCartItem);
        }
    }
    public void removeItemFromCart(Long cartId, String itemId, int quantityToRemove) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(cartId);
        if (optionalShoppingCart.isEmpty()) {
            throw new EntityNotFoundException("Cart with ID " + cartId + " not found");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        CartItemKey cartItemKey = new CartItemKey(shoppingCart.getCartID(), itemId);


        //check if the recorde is already in the cart (if the key (item_id , cart_id) exists )
        CartItem cartItem = cartItemsRepository.findById(cartItemKey)
                .orElseThrow(() -> new IllegalArgumentException("Item does not exist in the cart."));

        //check if newQuantity is a positive number
        int newQuantity = cartItem.getQuantity() - quantityToRemove;
        if (newQuantity <= 0) {
            //If the quantity is 0 or less - delete the record.
            cartItemsRepository.delete(cartItem);
            //If cart is empty - delete the cart

        } else {
            //quantity update
            cartItem.setQuantity(newQuantity);
            cartItemsRepository.save(cartItem);
        }
    }

    public List<CartItemDTO> getItemsByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemsRepository.findById_CartID(cartId);
        if (cartItems.isEmpty()) {
            return new ArrayList<>();
        }
        return cartItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private CartItemDTO mapToDTO(CartItem cartItem) {
        Item item = cartItem.getItem();
        if (item == null) {
            throw new IllegalStateException("Item not found for cart item ID: " + cartItem.getId().getItemID());
        }

        Double totalMin = (item.getLowestPrice() != null)
                ? cartItem.getQuantity() * item.getLowestPrice()
                : null;

        Double totalMax = (item.getHighestPrice() != null)
                ? cartItem.getQuantity() * item.getHighestPrice()
                : null;

        return new CartItemDTO(
                item.getItemID(),
                item.getItemName(),
                item.getImageUrl(),
                cartItem.getQuantity(),
                totalMin,
                totalMax,
                item.getLowestPrice(),
                item.getHighestPrice()
        );
    }

}