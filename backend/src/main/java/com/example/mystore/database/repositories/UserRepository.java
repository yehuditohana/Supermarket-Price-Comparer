package com.example.mystore.database.repositories;

import com.example.mystore.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

// Checks if a user exists with the specified email address (mast be unique).
    boolean existsByEmail(String email);

// Finds a user by their email address.
    Optional<User> findByEmail(String email);

// Finds a user by their session number.
    Optional<User> findBySessionNumber(String sessionNumber);
}