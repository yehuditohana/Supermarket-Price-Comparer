package com.example.mystore.database.entities;

import com.example.mystore.services.CartStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// Represents the shopping cart entity in the application.
// This table stores all the shopping cart details for each user.
@Entity
@Table(name = "SHOPPING_CART" ,
     indexes = @Index(name = "idx_user_id" , columnList = "user_id"))
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated identifier
    @Column(name = "cart_id")
    private Long cartID;  // Unique identifier for the shopping cart (Primary key)

    // Many-to-one relationship with `User`.
// A shopping cart is owned by a single user, and each user can have multiple shopping carts.
    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "user_id")
    private User user;  // Foreign key referencing the User table (User who owns the cart)

    // One-to-many relationship with `CartItem`.
// A single shopping cart can contain multiple `CartItem` entries.
    @OneToMany (mappedBy = "shoppingCart", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "cart_name")
    private String cartName ; // When a user saves a shopping cart to the archive, they can assign a name to it.

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Date when the shopping cart was created

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // Last update date of the cart


    // Note: Each user can have only one active cart at any given time.
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status;  // Cart status (Archived / Active).

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Long getCartID() {
        return cartID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }
}
