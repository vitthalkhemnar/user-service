package com.ecom.user.service;

import org.springframework.stereotype.Service;

import com.ecom.user.dto.UserResponse;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;

	public UserResponse getUserProfile() {
		String username = CommonUtil.getCurrentUsername();
		return userRepo.findByUsername(username).map(this::mapToResponse).get();
	}
	
	public User getCurrentUser() {
		String username = CommonUtil.getCurrentUsername();
		return userRepo.findByUsername(username).orElseThrow(RuntimeException::new);
	}
	
	private UserResponse mapToResponse(User user) {
		if (user == null) {
            return null;
        }

        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone()
        );
	}
}