package com.ecom.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.dto.UserResponse;
import com.ecom.user.dto.UserUpdate;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.util.AppConstants;
import com.ecom.user.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public List<UserResponse> getAllUsers() {
		return userRepo.findAll().stream().map(this::mapToResponse).toList();
	}
	
	@Transactional
	public UserResponse updateUserById(UserUpdate req) {
		User user = userRepo.findById(req.id()).orElseThrow(RuntimeException::new);
		
		user.setUsername(req.username());
		user.setFirstName(req.firstName());
		user.setLastName(req.lastName());
		user.setPhone(req.phone());
		
		if(req.isAdmin()) {
			user.setRoles(AppConstants.ROLE_ADMIN);
		}
		
		User savedUser = userRepo.save(user);
		return mapToResponse(savedUser);
	}
	
	public boolean deleteUser(Long userId) {
		try {
			userRepo.deleteById(userId);
			log.info("User with userId: {} deleted successfully.", userId);
			return true;
		} catch (Exception e) {
			log.error("Exception while deleting user.", e);
		}
		return false;
	}
	
	private UserResponse mapToResponse(User user) {
		if (user == null) {
            return null;
        }
		
		boolean isAdmin = AppConstants.ROLE_ADMIN.equals(user.getRoles());

        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            isAdmin
        );
	}
}