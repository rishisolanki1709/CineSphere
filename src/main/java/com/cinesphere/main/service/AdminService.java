package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.AdminDashboardDTO;
import com.cinesphere.main.entity.User;

public interface AdminService {
	public AdminDashboardDTO getDashboard();

	public List<User> getAllUsers();
}
