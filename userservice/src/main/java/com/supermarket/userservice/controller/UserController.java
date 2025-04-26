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

    // routes for admin
    @GetMapping("/admin/getAllUsers")
    public List<User> getAllUsers() {
        return service.getAllUser();
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public String deleteUserById(@PathVariable int id) throws Exception {
        service.deleteUserById(id);
        return "user deleted successfully";
    }

    @PutMapping("/admin/updateRole/{id}")
    public User updateUserRole(@PathVariable int id, @RequestParam Role newRole) {
        User user = service.updateUserRole(id, newRole);
        return user;
    }


}
