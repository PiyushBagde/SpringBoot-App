package com.supermarket.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.supermarket.userservice.ResourceNotFoundException;
import com.supermarket.userservice.model.User;
import com.supermarket.userservice.model.UserPrincipal;
import com.supermarket.userservice.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(username);

		if (user == null) {
			System.out.println("User not found");
			throw new ResourceNotFoundException("user not found");
		}
		return new UserPrincipal(user);
	}

}
