package com.cinesphere.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.dto.UserRegisterResponse;
import com.cinesphere.main.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@GetMapping("test")
	public String testApi() {
		return "Ok";
	}

	private final UserServiceImpl userService;

	public AuthController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest request) {

		userService.registerUser(request);

		UserRegisterResponse response = new UserRegisterResponse();
		response.setMessage("User registered successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
