package com.nexlesoft.spring.security.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.nexlesoft.spring.security.jwt.model.Token;

public interface RefreshTokenRepository extends JpaRepository<Token, Integer> {
	
	Optional<Token> findByRefreshToken(String refreshToken);
	
	@Modifying
    void deleteByUserId(Integer userId);

}
