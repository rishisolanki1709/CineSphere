package com.cinesphere.main.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cinesphere.main.customException.EmailAlreadyExistsException;
import com.cinesphere.main.dto.UserRegisterRequest;
import com.cinesphere.main.entity.User;
import com.cinesphere.main.repository.UserRepository;
import com.cinesphere.main.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@Override
	public void registerUser(UserRegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());

		userRepository.save(user);
	}
}