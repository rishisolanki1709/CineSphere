package com.cinesphere.main.service;

import java.util.List;

import com.cinesphere.main.dto.AdminDashboardResponseDTO;
import com.cinesphere.main.dto.UserResponseDTO;

public interface AdminService {
	public AdminDashboardResponseDTO getDashboard(String date);

	public List<UserResponseDTO> getAllUsers();
}
