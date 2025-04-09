package com.supermarket.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/customer/getUser/{id}")
	public User getUserById(@PathVariable int id) {
		User user = service.getUserById(id);
		return user;
	}

	@GetMapping("/admin/getAllUsers")
	public List<User> getAllUsers() {
		return service.getAllUser();
	}

	@DeleteMapping("/admin/deleteUser/{id}")
	public String deleteUserById(@PathVariable int id) throws Exception {
		service.deleteUserById(id);
		return "user deleted successfully";
	}

	@PutMapping("/admin/updateUseremail/{id}")
	public User updateUsername(@PathVariable int id, @RequestBody User user) {
		return service.updateUserEmail(id,user);
		
	}
	
	
	@PutMapping("/admin/updateRole/{id}")
	public User updateUserRole(@PathVariable int id, @RequestParam Role newRole) {
		User user = service.updateUserRole(id, newRole);
		return user;
	}
}
