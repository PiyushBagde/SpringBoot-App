package com.supermarket.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.supermarket.userservice.exception.ResourceNotFoundException;
import com.supermarket.userservice.model.Role;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.repository.UserRepository;
import com.supermarket.userservice.security.JWTService;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	// create new user
	public User register(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole(Role.CUSTOMER);
		return userRepository.save(user);
	}

	public User getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("usernot found");
		}

		return user.get();
	}

	// get all user details
	public List<User> getAllUser() {
		List<User> usersList = new ArrayList<>();
		Iterable<User> iterable = userRepository.findAll();
		iterable.forEach(usersList::add);

		return usersList;
	}

	// update user mail using id
	public User updateUserEmail(int id, User updatedUser) {
		Optional<User> existinguser = userRepository.findById(id);

		if (!existinguser.isPresent()) {
			throw new ResourceNotFoundException("not found");
		}

		User user = existinguser.get();

		user.setEmail(updatedUser.getEmail());
		return userRepository.save(user);
	}

	// delete user by id
	public void deleteUserById(int id) throws Exception {
		if (!userRepository.existsById(id)) {
			throw new Exception("fdas");
		}
		userRepository.deleteById(id);
	}

	// update user role(access to admin only)
	public User updateUserRole(int id, Role newRole) {
		Optional<User> existinguser = userRepository.findById(id);

		if (!existinguser.isPresent()) {
			throw new ResourceNotFoundException("not found");
		}

		User user = existinguser.get();

		user.setRole(newRole);

		return userRepository.save(user);
	}
	public String verify(User user) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

		if (authentication.isAuthenticated()) {
			System.out.println("** user email: " + user.getEmail());
			System.out.println("** called verify() from service");

			// ✅This will now include role in token
			return jwtService.generateToken(user.getEmail());
		}

		System.out.println("** auth failed in userservice");
		return "auth fail";
	}


}
