package com.example.mystore.database.entities;

import jakarta.persistence.*;

// This class defines the `cart_items` table.
// This entity represents the presence of a specific product in a specific cart
// (one or more instances, depending on the quantity field).
@Entity
@Table(name = "CART_ITEM" ,
// `idx_cart_id` - an index on the `cart_id` column to optimize queries filtering by cart.
// `idx_item_id_cart` - an index on the `item_id` column to improve lookup performance for items within carts.
   indexes = {
        @Index(name = "idx_cart_id" , columnList = "cart_id"),
        @Index(name = "idx_item_id_cart", columnList = "item_id")
}
)
public class CartItem {
    @EmbeddedId
    private CartItemKey id;  // Composite primary key consisting of cartID and itemID
// Defines a many-to-one relationship with the `ShoppingCart` table.
// A single cart item entity may exist across multiple shopping carts, each containing different sets of items.
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id" , insertable = false , updatable = false)
    private ShoppingCart shoppingCart;
// Defines a many-to-one relationship with the `Item` table.
// A single item can be associated with multiple cart entries across different shopping carts.
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id" , insertable = false , updatable = false)
    private Item item ;
    @Column(name = "quantity")
    private Integer quantity;  // Quantity of the item in the cart



    public CartItemKey getId() {
        return id;
    }

    public void setId(CartItemKey id) {
        this.id = id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
