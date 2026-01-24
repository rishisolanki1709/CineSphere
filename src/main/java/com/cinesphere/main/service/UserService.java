package com.cinesphere.main.service;

import com.cinesphere.main.customException.EmailAlreadyExistsException;
import com.cinesphere.main.dto.UserRegisterRequest;

public interface UserService {
	void registerUser(UserRegisterRequest request) throws EmailAlreadyExistsException;
}
