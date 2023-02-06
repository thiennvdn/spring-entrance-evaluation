package com.nexlesoft.spring.security.jwt.payload;

import com.nexlesoft.spring.security.jwt.dto.UserDto;

public class LoginResponse {
	
    private String token;
    private String refreshToken;
    private UserDto user;
    
	public LoginResponse(String token, String refreshToken, UserDto user) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}
