package com.supermarket.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService service;

	@PostMapping("/login")
	public String login(@RequestBody User user) {
		return service.verify(user);
	}
	
	@PostMapping("/register")
	public User createUser(@RequestBody User user) {
		return service.register(user);
		
	}

	@GetMapping("/getUser/{id}")
	public User getUserById(@PathVariable int id) {
		User user = service.getUserById(id);
		return user;
	}

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return service.getAllUser();
	}

	@DeleteMapping("/deleteUser/{id}")
	public String deleteUserById(@PathVariable int id) throws Exception {
		service.deleteUserById(id);
		return "user deleted successfully";
	}

	@PutMapping("/updateUseremail/{id}")
	public User updateUsername(@PathVariable int id, @RequestBody User user) {
		return service.updateUserEmail(id,user);
		
	}
	
	@PutMapping("/updateRole/{id}")
	public User updateUserRole(@PathVariable int id, @RequestParam Role newRole) {
		User user = service.updateUserRole(id, newRole);
		return user;
	}

}
