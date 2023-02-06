package com.nexlesoft.spring.security.jwt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nexlesoft.spring.security.jwt.model.User;
import com.nexlesoft.spring.security.jwt.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //check "ten dang nhap" is exits
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Tài khoản: " + username + " không tồn tại");
        }
        return UserDetailsImpl.build(user);
    }
}
