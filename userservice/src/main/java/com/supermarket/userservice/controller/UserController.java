package com.supermarket.userservice.controller;

import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@Validated // enables parameter validation
public class UserController {
    @Autowired
    private UserService service;

    // route for biller and user
    @PutMapping("/biller-customer/updatePassword")
    public String updatePassword(
            @RequestHeader("X-UserId") int userId,
            @RequestParam @NotBlank(message = "New password cannot be blank")
            @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
            String newPassword) {
        return service.updateUserPassword(userId, newPassword);
    }

    // routes for admin
    @GetMapping("/admin/getAllUsers")
    public List<User> getAllUsers() {
        return service.getAllUser();
    }

    @DeleteMapping("/admin/deleteUser/{userId}")
    public String deleteUser(
            @PathVariable @Min(value = 1, message = "User ID must be a positive number") int userId) {
        service.deleteUserById(userId);
        return "user deleted successfully";
    }

    @PutMapping("/admin/updateRole/{id}")
    public User updateUserRole(@PathVariable @Min(value = 1, message = "User ID must be a positive number") int id,
                               @RequestParam @NotNull(message = "New role cannot be null") Role newRole) {
        User user = service.updateUserRole(id, newRole);
        return user;
    }

    @GetMapping("/admin/getUser/{userId}")
    public User getUser(
            @PathVariable @Min(value = 1, message = "User ID must be a positive number") int userId) {
        return service.getUserByUserId(userId);
    }
}
