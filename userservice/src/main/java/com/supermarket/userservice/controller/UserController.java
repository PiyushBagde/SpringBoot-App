package com.supermarket.userservice.controller;

import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    // route for biller and user
    @PutMapping("/biller-customer/updatePassword")
    public String updatePassword(@RequestHeader("X-UserId") int userId, @RequestParam String newPassword) {
        return service.updateUserPassword(userId, newPassword);
    }

    // routes for admin
    @GetMapping("/admin/getAllUsers")
    public List<User> getAllUsers() {
        return service.getAllUser();
    }

    @DeleteMapping("/admin/deleteUser/{userId}")
    public String deleteUser(@PathVariable int userId) throws Exception {
        service.deleteUserById(userId);
        return "user deleted successfully";
    }

    @PutMapping("/admin/updateRole/{id}")
    public User updateUserRole(@PathVariable int id, @RequestParam Role newRole) {
        User user = service.updateUserRole(id, newRole);
        return user;
    }

    @GetMapping("/admin/getUser/{userId}")
    public User getUser(@PathVariable int userId) {
        return service.getUserByUserId(userId);
    }
}
