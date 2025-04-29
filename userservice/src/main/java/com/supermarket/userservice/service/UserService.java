package com.supermarket.userservice.service;

import com.supermarket.userservice.exception.AuthenticationFailedException;
import com.supermarket.userservice.exception.OperationFailedException;
import com.supermarket.userservice.exception.ResourceNotFoundException;
import com.supermarket.userservice.exception.UserAlreadyExistsException;
import com.supermarket.userservice.dto.LoginRequest;
import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.repository.UserRepository;
import com.supermarket.userservice.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;

    // create new user
    public User register(User user) {
        // check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            // Throw specific exception for duplicate email
            throw new UserAlreadyExistsException("Registration failed: Email already in use: " + existingUser.get().getEmail());
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER); // set default role as CUSTOMER
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            // Throw specific exception for operation failure
            throw new OperationFailedException("Failed to register user with email: " + user.getEmail(), e);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred during registration for email: " + user.getEmail(), e);
        }
    }

    public List<User> getAllUser() {
        List<User> usersList = userRepository.findAll();

        if (usersList.isEmpty()) {
            throw new ResourceNotFoundException("No users found in the database.");
        }
        return usersList;
    }

    public String updateUserPassword(int userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be blank.");
        }
        if (encoder.matches(newPassword, user.getPassword())) {
            return "Password remain unchanged.";
        }
        try {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return "Password updated successfully.";
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update password for user ID: " + userId, e);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred during password update for user ID: " + userId, e);
        }
    }


    // delete user by id
    public void deleteUserById(int userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Cannot delete. User not found with ID: " + userId);
        }
        try {
            userRepository.deleteById(userId);
        } catch (DataAccessException e) {
            // Throw specific exception for operation failure
            throw new OperationFailedException("Failed to delete user with ID: " + userId, e);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred during deletion for user ID: " + userId, e);
        }
    }

    // update user role(access to admin only)
    public User updateUserRole(int userId, Role newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId + " for role update."));
        user.setRole(newRole);
        user.setRole(newRole);
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new OperationFailedException("Failed to update role for user ID: " + userId, e);
        } catch (Exception e) {
            throw new OperationFailedException("An unexpected error occurred during role update for user ID: " + userId, e);
        }
    }

    public User getUserByUserId(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    public String verify(LoginRequest user) {
        System.out.println("verify is running for email: " + user.getEmail());

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException e) {
            // Catch exception for bad credentials
            System.out.println("** Authentication failed: Bad credentials for " + user.getEmail());
            throw new AuthenticationFailedException("Authentication failed: Invalid email or password.");
        } catch (Exception e) {
            // Catch authentication exceptions
            System.err.println("** An unexpected error occurred during authentication for " + user.getEmail() + ": " + e.getMessage());
            throw new OperationFailedException("An unexpected error occurred during authentication.", e);
        }

        if (authentication.isAuthenticated()) {
            System.out.println("** Authentication successful for: " + user.getEmail());
            // Use orElseThrow with ResourceNotFoundException, because user should exist.
            User authenticatedUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database: " + user.getEmail()));

            System.out.println("** Generating token for userId: " + authenticatedUser.getId());
            // Generate token using authenticated user details
            return jwtService.generateToken(authenticatedUser.getEmail(), authenticatedUser.getId());
        } else {
            System.out.println("** Authentication failed (post-check) for " + user.getEmail());
            throw new AuthenticationFailedException("Authentication failed for an unknown reason.");
        }
    }
}