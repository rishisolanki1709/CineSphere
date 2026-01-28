package com.cinesphere.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.LoginRequest;
import com.cinesphere.main.dto.LoginResponse;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.security.JwtUtil;
import com.cinesphere.main.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	@Override
	public LoginResponse login(LoginRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));
		String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

		System.out.println("JWT Token " + token);
		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setMessage("Login successful");

		return response;
	}

}
