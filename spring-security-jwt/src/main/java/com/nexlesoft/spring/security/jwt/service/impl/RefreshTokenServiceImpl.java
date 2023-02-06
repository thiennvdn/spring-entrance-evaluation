package com.nexlesoft.spring.security.jwt.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexlesoft.spring.security.jwt.exception.TokenRefreshException;
import com.nexlesoft.spring.security.jwt.model.Token;
import com.nexlesoft.spring.security.jwt.repository.RefreshTokenRepository;
import com.nexlesoft.spring.security.jwt.service.RefreshTokenService;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Value("${nexlesoft.app.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;


    @Override
    public Optional<Token> findByToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    @Override 
    public Token createRefreshToken(Integer userId) {
        Token refreshToken = new Token();

        refreshToken.setUserId(userId);
        refreshToken.setExpiresIn(Instant.now().plusMillis(jwtRefreshExpirationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public Token verifyExpiration(Token token) {
        if (token.getExpiresIn().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    public void deleteByUserId(Integer userId) {
         refreshTokenRepository.deleteByUserId(userId);
    }
}
