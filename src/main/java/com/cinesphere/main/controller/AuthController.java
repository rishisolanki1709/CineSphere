package com.cinesphere.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinesphere.main.dto.ApiResponseDTO;
import com.cinesphere.main.dto.LoginRequestDTO;
import com.cinesphere.main.dto.LoginResponseDTO;
import com.cinesphere.main.dto.UserRegisterRequestDTO;
import com.cinesphere.main.dto.UserRegisterResponseDTO;
import com.cinesphere.main.service.impl.AuthServiceImpl;
import com.cinesphere.main.service.impl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthServiceImpl authService;
	private final UserServiceImpl userService;

	public AuthController(AuthServiceImpl authService, UserServiceImpl userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
		System.out.println("Email : " + request.getEmail() + " Password : " + request.getPassword());
		LoginResponseDTO loginResponse = authService.login(request);

		return ResponseEntity.ok(new ApiResponseDTO<>(true, "Login Successful", loginResponse));
	}

	@GetMapping("/test")
	public String testApi() {
		return "Ok";
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponseDTO<UserRegisterResponseDTO>> register(@Valid @RequestBody UserRegisterRequestDTO request) {

		userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponseDTO<>(true, "User Rregistered Successfully", null));
	}
}
