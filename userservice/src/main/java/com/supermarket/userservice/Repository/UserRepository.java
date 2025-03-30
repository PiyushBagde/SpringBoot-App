package com.supermarket.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supermarket.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{


}
