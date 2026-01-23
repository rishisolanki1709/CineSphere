package com.cinesphere.main.controller;

import org.springframework.stereotype.Controller;

import com.cinesphere.main.service.UserService;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

}
