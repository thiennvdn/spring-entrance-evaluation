package com.nexlesoft.spring.security.jwt.service;

import java.util.Optional;

import com.nexlesoft.spring.security.jwt.model.Token;

public interface RefreshTokenService {
	
    Optional<Token> findByToken(String refreshToken);

    Token createRefreshToken(Integer userId);

    Token verifyExpiration(Token token);

    void deleteByUserId(Integer userId);
}
