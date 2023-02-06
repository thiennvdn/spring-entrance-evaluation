package com.nexlesoft.spring.security.jwt.service;

import java.util.List;

import com.nexlesoft.spring.security.jwt.model.User;

public interface UserService {
    List<User> getAllUser();
    
    int save(User user);
    
    User getUserByEmail(String email);
}
