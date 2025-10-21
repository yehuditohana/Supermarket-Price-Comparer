package com.example.mystore.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
// This class represents a composite key for the `CartItem` table.
// The key is composed of two fields:
// `cart_id` (cart identifier) and `item_id` (item identifier).
@Embeddable
public class CartItemKey implements Serializable {

    @Column(name = "cart_id")
    private Long cartID;
    @Column(name = "item_id")
    private String itemID;

    public CartItemKey() {
    }

    public CartItemKey(Long cartID, String itemID) {
        this.cartID = cartID;
        this.itemID = itemID;
    }
   @Override
    public int hashCode() {

        return Objects.hash(cartID, itemID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        return Objects.equals(this.cartID, ((CartItemKey) obj).cartID)
                && Objects.equals(this.itemID , ((CartItemKey) obj).itemID);
    }

    public Long getCartID() {
        return cartID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}

