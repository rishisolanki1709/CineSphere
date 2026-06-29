package com.cinesphere.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cinesphere.main.dto.LoginRequestDTO;
import com.cinesphere.main.dto.LoginResponseDTO;
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
	public LoginResponseDTO login(LoginRequestDTO request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User Not Found"));
		String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

		LoginResponseDTO response = new LoginResponseDTO();
		response.setToken(token);
		response.setEmail(user.getEmail());
		response.setName(user.getName());
		response.setPhoneNumber(user.getPhone());
		response.setMessage("Login Successfully");

		return response;
	}

}
