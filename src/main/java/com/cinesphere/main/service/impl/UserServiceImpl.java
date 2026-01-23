package com.cinesphere.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cinesphere.main.customException.EmailAlreadyExistsException;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.service.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public User registerUser(User user) throws EmailAlreadyExistsException {
		try {
			if (userRepository.existsByEmail(user.getEmail())) {
				throw new EmailAlreadyExistsException("Email already registered");
			}
		} catch (EmailAlreadyExistsException e) {
			e.printStackTrace();
		}
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

}
