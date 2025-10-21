package com.example.mystore.dto.api.response;

import java.time.LocalDateTime;
/**
 * CartDto represents a summary view of a user's shopping cart.
 *
 * Fields:
 * - id: Unique identifier of the cart.
 * - name: Name  assigned to the cart (Can be set when archiving a cart).
 * - updatedAt: Timestamp of the last update made to the cart.
 *
 * Used for presenting archived or active carts user.
 */
public class CartDto {
    Long id;
    String name;
    LocalDateTime updatedAt;

    public CartDto(Long id, String name, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
