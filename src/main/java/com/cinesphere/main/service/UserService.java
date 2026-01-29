package com.cinesphere.main.service;

import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.exception.EmailAlreadyExistsException;

public interface UserService {
	void registerUser(UserRegisterRequest request) throws EmailAlreadyExistsException;

	public void createAdmin(UserRegisterRequest request);
}
