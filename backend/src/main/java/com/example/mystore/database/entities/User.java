package com.example.mystore.database.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * User entity represents a user record in the database.
 *
 * Mapped to the "USER" table.
 *
 * Relationships:
 * - One-to-many relationship with ShoppingCart: A user can have multiple shopping carts.
 */
@Entity
@Table(
        name = "\"USER\"",
        indexes = {
                @Index(name = "email_index", columnList = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;  // Unique identifier for the user (Primary key)

    // One-to-many relationship with `ShoppingCart`. A user can have multiple shopping carts.
    @OneToMany (mappedBy = "user")
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();
    @Column(name = "user_name")
    private String username;  // Username for the user

    @Column(name = "email" , unique = true)
    private String email;  //Email address of the user, which must be unique

    @Column(name = "password_hash")
    private String passwordHash;  // Encrypted password

    @Column(name = "is_admin")
    private Boolean isAdmin;  // Whether the user is an admin

    @Column(name = "create_at")
    private LocalDateTime createdAt;  // Account creation date

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // Last update date

    @Column(name = "status")
    private Boolean status;  // Whether the account is active or not (boolean value)

    @Column(name = "session_number")
    private String sessionNumber; // Session number for tracking the user's session


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }



    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
}
