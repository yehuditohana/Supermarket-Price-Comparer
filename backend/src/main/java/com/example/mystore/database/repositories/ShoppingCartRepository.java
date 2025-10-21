package com.example.mystore.database.repositories;

import com.example.mystore.database.entities.ShoppingCart;
import com.example.mystore.database.entities.User;
import com.example.mystore.services.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    // Finds all shopping carts associated with a specific user by user ID.
    List<ShoppingCart> findByUserUserID(Long userId);

    // Finds a shopping cart for a specific user by user ID and cart status.
    Optional<ShoppingCart> findByUser_UserIDAndStatus(Long userId, CartStatus cartStatus);

}