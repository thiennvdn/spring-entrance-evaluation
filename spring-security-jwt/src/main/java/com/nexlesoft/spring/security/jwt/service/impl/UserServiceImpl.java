package com.nexlesoft.spring.security.jwt.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexlesoft.spring.security.jwt.model.User;
import com.nexlesoft.spring.security.jwt.repository.UserRepository;
import com.nexlesoft.spring.security.jwt.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

	@Override
	public int save(User user) {
		if(user.getPassword().length() < 8 || user.getPassword().length() > 30) {
			return 0;
		}
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedAt(new Date());
		userRepository.save(user);
		return 1;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}
}
