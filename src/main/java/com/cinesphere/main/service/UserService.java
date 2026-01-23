package com.cinesphere.main.service;

import com.cinesphere.main.customException.EmailAlreadyExistsException;
import com.cinesphere.main.entity.User;

public interface UserService {
	User registerUser(User user) throws EmailAlreadyExistsException;
}
