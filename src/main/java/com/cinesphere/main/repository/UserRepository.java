package com.cinesphere.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	List<User> findByRole(String role);
	boolean existsByEmail(String email);
}
