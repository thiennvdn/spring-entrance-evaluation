package com.nexlesoft.spring.security.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexlesoft.spring.security.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String email);

    Boolean existsByEmail(String email);
    
}
