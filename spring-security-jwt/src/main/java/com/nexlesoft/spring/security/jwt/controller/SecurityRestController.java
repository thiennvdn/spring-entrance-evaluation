package com.nexlesoft.spring.security.jwt.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexlesoft.spring.security.jwt.dto.UserDto;
import com.nexlesoft.spring.security.jwt.exception.TokenRefreshException;
import com.nexlesoft.spring.security.jwt.jwt.JwtTokenUtil;
import com.nexlesoft.spring.security.jwt.model.Token;
import com.nexlesoft.spring.security.jwt.model.User;
import com.nexlesoft.spring.security.jwt.payload.LoginRequest;
import com.nexlesoft.spring.security.jwt.payload.LoginResponse;
import com.nexlesoft.spring.security.jwt.payload.TokenRefreshRequest;
import com.nexlesoft.spring.security.jwt.payload.TokenRefreshResponse;
import com.nexlesoft.spring.security.jwt.service.RefreshTokenService;
import com.nexlesoft.spring.security.jwt.service.UserService;
import com.nexlesoft.spring.security.jwt.service.impl.UserDetailsImpl;

@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("api")
public class SecurityRestController {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(loginRequest.getEmail());
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
        User user = userService.getUserByEmail(userDetails.getUsername());
        Token refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(
                new LoginResponse(
                        jwt,
                        refreshToken.getRefreshToken(),
                        new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDisplayName(), user.getEmail())
                        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(user -> {
                    String token = jwtTokenUtil.generateJwtToken(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid User user) {
        int result = userService.save(user);
        if (result == 0) {
        	return new ResponseEntity<>("Đăng ký thất bại!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDisplayName(), user.getEmail()));
    }
    
    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser() {
      UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Integer userId = userDetails.getId();
      refreshTokenService.deleteByUserId(userId);
      return ResponseEntity.ok("Log out successful!");
    }
}
