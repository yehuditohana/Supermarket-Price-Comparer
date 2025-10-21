package com.example.mystore.database.repositories;

import com.example.mystore.database.entities.CartItem;
import com.example.mystore.database.entities.CartItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {
    List<CartItem> findById_CartID(Long cartId); //find all the items in cart (by cart id)
}