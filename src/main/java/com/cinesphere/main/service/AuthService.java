package com.cinesphere.main.service;

import com.cinesphere.main.dto.LoginRequest;
import com.cinesphere.main.dto.LoginResponse;

public interface AuthService {
	public LoginResponse login(LoginRequest request);
}
