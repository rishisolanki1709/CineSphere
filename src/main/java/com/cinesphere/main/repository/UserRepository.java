package com.cinesphere.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinesphere.main.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
}
