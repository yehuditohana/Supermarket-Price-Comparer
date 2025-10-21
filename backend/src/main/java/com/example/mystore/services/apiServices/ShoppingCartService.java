package com.example.mystore.services.apiServices;

import com.example.mystore.database.repositories.CartItemRepository;
import com.example.mystore.database.entities.ShoppingCart;
import com.example.mystore.database.entities.User;
import com.example.mystore.database.repositories.ShoppingCartRepository;
import com.example.mystore.database.repositories.UserRepository;
import com.example.mystore.dto.api.response.CartDto;
import com.example.mystore.dto.api.response.UserSummaryDTO;
import com.example.mystore.services.CartStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemsRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, CartItemRepository cartItemsRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.cartItemsRepository = cartItemsRepository;
    }
    /**
     * Retrieves the active cart ID for a given user.
     * If no active cart exists, creates a new one.
     *
     * @param userId the ID of the user
     * @return the ID of the active (or newly created) cart
     */
    public Long getActiveCartForUser(Long userId){
        Optional<User> optionalUser= userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
        Optional<ShoppingCart> optional = shoppingCartRepository.findByUser_UserIDAndStatus(userId, CartStatus.ACTIVE);
        if(optional.isEmpty()){
            // Create a new cart
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(optionalUser.get());
            shoppingCart.setCreatedAt(LocalDateTime.now());
            shoppingCart.setStatus(CartStatus.ACTIVE);
            ShoppingCart newCart = shoppingCartRepository.save(shoppingCart);
            return newCart.getCartID();
        }
        else return optional.get().getCartID();
    }
    /**
     * Creates a new cart for a registered user if no active cart exists.
     *
     * @param userSummaryDTO summary DTO of the user
     * @return the ID of the created or existing cart
     */
    // There is no user validation - assumed that cart creation
    //Is done through a registered user.
    public Long createCartForUser(UserSummaryDTO userSummaryDTO) {
       Long userId =  userSummaryDTO.getUserId();
        Optional<User> optionalUser= userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
        User user = optionalUser.get();
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser_UserIDAndStatus(userId, CartStatus.ACTIVE);

        if(optionalShoppingCart.isPresent()){
            return optionalShoppingCart.get().getCartID();
        }
        // Create a new cart
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCreatedAt(LocalDateTime.now());
        shoppingCart.setStatus(CartStatus.ACTIVE);

        //Saving the cart in the database
        ShoppingCart newCart = shoppingCartRepository.save(shoppingCart);
        return newCart.getCartID();
    }

    //Changing cart status from "Active" to "Archived"
    public void archiveCart(Long cartId, String cartName) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(cartId);
        if(optionalShoppingCart.isEmpty()){
            throw new IllegalArgumentException("No cart found.");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();

        shoppingCart.setStatus(CartStatus.ARCHIVED);
        if(cartName != null && cartName != ""){
            shoppingCart.setCartName(cartName);
        }
        shoppingCart.setUpdatedAt(LocalDateTime.now());
        shoppingCartRepository.save(shoppingCart);
    }
    /**
     * Deletes a cart along with its associated cart items (CascadeType.REMOVE).
     *
     * @param cartId the ID of the cart to delete
     */
    //Deletes the cart and all rows from itemCart table with the same cart_id
    public void deleteCart(Long cartId) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(cartId);
        if(optionalShoppingCart.isEmpty()){
            throw new IllegalArgumentException("No cart found.");
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();

        // Automatically deletes all cart items due to CascadeType.REMOVE
        shoppingCartRepository.delete(shoppingCart);
    }

    /**
     * Retrieves all archived carts for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of archived CartDto objects
     */
    //Returns all user's carts.
    public List<CartDto> getArchivedCartsByUser(Long userId) {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUserUserID(userId);
        List<CartDto> cartDtos = new ArrayList<>();
        for(ShoppingCart shoppingCart: shoppingCarts){
            if(shoppingCart.getStatus() == CartStatus.ARCHIVED){
                CartDto cartDto = new CartDto(shoppingCart.getCartID() , shoppingCart.getCartName() , shoppingCart.getUpdatedAt());
                cartDtos.add(cartDto);
            }
        }
       return cartDtos;
    }
    /**
     * Reactivates a previously archived cart by changing its status back to ACTIVE.
     * Deletes any existing active cart for the user.
     *
     * @param cartId the ID of the cart to activate
     */
    //Loading a cart from history to active mode
    public void activateCart(Long cartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("No cart found."));

        if (!shoppingCart.getStatus().equals(CartStatus.ARCHIVED)) {
            throw new IllegalArgumentException("No cart found for loading.");
        }
        Long userId = shoppingCart.getUser().getUserID();

        Optional<ShoppingCart> optional = shoppingCartRepository.findByUser_UserIDAndStatus(userId, CartStatus.ACTIVE);
        //If there is an active cart - it will be deleted.
        if(optional.isPresent()){
            ShoppingCart shoppingCartActive = optional.get();
            shoppingCartRepository.delete(shoppingCartActive);
        }
        shoppingCart.setStatus(CartStatus.ACTIVE);
        shoppingCartRepository.save(shoppingCart);
    }

}