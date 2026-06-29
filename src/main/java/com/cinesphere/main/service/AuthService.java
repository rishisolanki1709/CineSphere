package com.cinesphere.main.service;

import com.cinesphere.main.dto.LoginRequestDTO;
import com.cinesphere.main.dto.LoginResponseDTO;

public interface AuthService {
	public LoginResponseDTO login(LoginRequestDTO request);
}
