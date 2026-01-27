package com.cinesphere.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.LoginRequest;
import com.cinesphere.main.dto.LoginResponse;
import com.cinesphere.main.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		String token = jwtUtil.generateToken(request.getEmail());
		System.out.println("JWT Token " + token);
		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setMessage("Login successful");

		return response;
	}

}
