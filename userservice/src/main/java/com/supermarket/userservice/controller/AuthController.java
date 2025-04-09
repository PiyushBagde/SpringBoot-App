package com.supermarket.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.userservice.model.User;
import com.supermarket.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class AuthController {
	@Autowired
	private UserService service;

	@PostMapping("/login")
	public String login(@RequestBody User user) {
		System.out.println("** login controller passed");
		return service.verify(user);
	}
	
	@PostMapping("/register")
	public User createUser(@RequestBody User user) {
		return service.register(user);
	}
}
