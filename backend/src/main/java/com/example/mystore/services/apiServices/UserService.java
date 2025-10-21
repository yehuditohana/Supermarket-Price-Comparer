package com.example.mystore.services.apiServices;

import com.example.mystore.api.exceptions.AuthenticationException;
import com.example.mystore.database.entities.User;
import com.example.mystore.database.repositories.UserRepository;
import com.example.mystore.dto.api.request.RegisterDTO;
import com.example.mystore.dto.api.response.UserSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Registers a new user with the provided information.
     *
     * @param dto the user registration data
     * @return the created UserSummaryDTO
     */
    public UserSummaryDTO registerUser(RegisterDTO dto) {
        validateUser(dto.getEmail() , dto.getPassword() , dto.getUsername());//Checking the correctness of values

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("האימייל כבר קיים במערכת");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setAdmin(false); // Default user is not an admin
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(true);  // Active account
        /// Creating a new session for the user
        String session = UUID.randomUUID().toString();
        user.setSessionNumber(session);
        // Saving the user to the database
        userRepository.save(user);
        // Returning user details for frontend
        return new UserSummaryDTO(user.getUserID(), user.getUsername(), user.getEmail() ,user.getSessionNumber());

    }
    /**
     * Validates email, password, and username fields.
     *
     * @param email the user's email
     * @param password the user's password
     * @param username the user's username
     */
    private void validateUser(String email , String password , String username) {
        if (email == null || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new IllegalArgumentException("כתובת אימייל לא תקינה");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("הסיסמה חייבת להכיל  8 תווים לפחות");
        }

        if (username== null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("שם משתמש הוא שדה חובה");
        }
    }
    /**
     * Authenticates a user by email and password.
     *
     * @param email the user's email
     * @param rawPassword the raw password input
     * @return the authenticated UserSummaryDTO
     */
    public UserSummaryDTO authenticateUser(String email, String rawPassword) {
        // Check if user exists
        Optional<User>  userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new AuthenticationException("פרטי התחברות שגויים");
        }
        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new AuthenticationException("פרטי התחברות שגויים");
        }
        // Successful authentication - create new session
        String session = UUID.randomUUID().toString();
        user.setSessionNumber(session);
        userRepository.save(user);
       return new UserSummaryDTO(user.getUserID(), user.getUsername() , user.getEmail(), user.getSessionNumber());

    }
    /**
     * Logs out a user by invalidating their session.
     *
     * @param sessionNumber the current session number
     */
    public void logoutUser(String sessionNumber) {
        Optional<User> optionalUser = userRepository.findBySessionNumber(sessionNumber);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setSessionNumber(null); // Reset the current session
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user); // Save the update
        } else {
            throw new IllegalArgumentException("Session לא נמצא או כבר נותק");
        }
    }



}