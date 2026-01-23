package com.cinesphere.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.service.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User registerUser(User user) {
		return null;
	}
	
}
