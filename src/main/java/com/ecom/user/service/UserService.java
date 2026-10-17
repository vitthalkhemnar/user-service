package com.ecom.user.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.constants.AppConstants;
import com.ecom.user.dto.UserResponse;
import com.ecom.user.dto.UserUpdateRequest;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.util.CommonUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserResponse getUserProfile() {
		return mapToResponse(getCurrentUser());
	}

	public UserResponse getUserByUsername(String identifier) {
		if (identifier == null || identifier.isBlank()) return null;
		User user = userRepository.findByPhone(identifier)
				.or(() -> userRepository.findByEmail(identifier))
				.orElse(null);
		return mapToResponse(user);
	}
	
	public User getCurrentUser() {
		String identifier = CommonUtil.getCurrentUsername();
		return userRepository.findByPhone(identifier)
				.or(() -> userRepository.findByEmail(identifier))
				.orElseThrow(RuntimeException::new);
	}
	
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapToResponse).toList();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public UserResponse updateUserById(UserUpdateRequest req) {
		User user = userRepository.findById(req.id()).orElseThrow(() -> new EntityNotFoundException());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication.getAuthorities() == null)
			throw new AccessDeniedException("Access denied.");
		
		boolean isCallerAdmin = authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
		
		User currentUser = getCurrentUser();
		boolean isSelf = currentUser.getId().equals(user.getId());
		
		if(!isCallerAdmin && !isSelf)
			throw new AccessDeniedException("Access denied: You cannot edit another user's profile.");
		
		if (isCallerAdmin && !isSelf) {
			user.setRoles(req.isAdmin() ? AppConstants.ROLE_ADMIN : AppConstants.ROLE_USER);
		}
		
		if (req.firstName() != null) user.setFirstName(req.firstName());
		if (req.lastName() != null) user.setLastName(req.lastName());
		if (req.phone() != null && !req.phone().isBlank()) user.setPhone(req.phone().trim());
		if (req.email() != null && !req.email().isBlank()) {
			String normalizedEmail = req.email().trim().toLowerCase();
			userRepository.findByEmail(normalizedEmail).ifPresent(existing -> {
				if (!existing.getId().equals(user.getId())) {
					throw new IllegalArgumentException("Email already in use: " + req.email());
				}
			});
			user.setEmail(normalizedEmail);
		}
		User savedUser = userRepository.save(user);
		return mapToResponse(savedUser);
	}
	
	public boolean deleteUser(Long userId) {
		try {
			userRepository.deleteById(userId);
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
		String displayName = user.getFirstName() != null ? user.getFirstName() : "";

        return new UserResponse(
            user.getId(),
            displayName,
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            isAdmin
        );
	}
}