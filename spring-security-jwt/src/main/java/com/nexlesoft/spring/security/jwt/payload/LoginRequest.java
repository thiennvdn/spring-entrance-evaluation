package com.nexlesoft.spring.security.jwt.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
	
    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Size(min = 12, max = 40)
    private String email;
    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, max = 40)
    private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
