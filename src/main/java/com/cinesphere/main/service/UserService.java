package com.cinesphere.main.service;

import com.cinesphere.main.dto.UserRegisterRequestDTO;
import com.cinesphere.main.exception.EmailAlreadyExistsException;

public interface UserService {
	void registerUser(UserRegisterRequestDTO request) throws EmailAlreadyExistsException;

	public void createAdmin(UserRegisterRequestDTO request);
}
