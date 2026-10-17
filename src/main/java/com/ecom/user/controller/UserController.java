package com.ecom.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.UserResponse;
import com.ecom.user.dto.UserUpdateRequest;
import com.ecom.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

	private final UserService userService;

	@Tag(name = "User Profile", description = "Endpoints for viewing and updating the authenticated user's profile")
	@Operation(
		summary = "Get current user profile",
		description = "Retrieves profile details (username, name, email, phone, role) for the currently authenticated user from their JWT token."
	)
	@GetMapping
	public ResponseEntity<UserResponse> getUserProfile() {
		UserResponse user = userService.getUserProfile();
		return ResponseEntity.ok(user);
	}

	@Tag(name = "User Profile", description = "Endpoints for viewing and updating the authenticated user's profile")
	@Operation(
		summary = "Update current user profile",
		description = "Updates profile details (first name, last name, phone) for the currently logged-in user."
	)
	@PutMapping
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest req) {
		UserResponse user = userService.updateUserById(req);
		return ResponseEntity.ok(user);
	}

	@Tag(name = "User Administration", description = "Administrative endpoints for managing and querying platform users")
	@Operation(
		summary = "Get user by username",
		description = "Finds and returns user account details by username. Useful for inter-service communication."
	)
	@GetMapping("/by-username/{username}")
	public ResponseEntity<UserResponse> getUserByUsername(
			@Parameter(description = "Username of the target user", required = true)
			@PathVariable("username") String username) {
		UserResponse user = userService.getUserByUsername(username);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}
	
	@Tag(name = "User Administration", description = "Administrative endpoints for managing and querying platform users")
	@Operation(
		summary = "Get user by userId",
		description = "Finds and returns user account details by userId. Useful for inter-service communication."
	)
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUserById(
			@Parameter(description = "Username of the target user", required = true)
			@PathVariable("userId") Long userId) {
		UserResponse user = userService.getUserById(userId);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@Tag(name = "User Administration", description = "Administrative endpoints for managing and querying platform users")
	@Operation(
		summary = "Get all users",
		description = "Returns a complete list of all registered platform users. Enforces ROLE_ADMIN authorization."
	)
	@GetMapping("/all-users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> user = userService.getAllUsers();
		return ResponseEntity.ok(user);
	}

	@Tag(name = "User Administration", description = "Administrative endpoints for managing and querying platform users")
	@Operation(
		summary = "Delete user by ID",
		description = "Permanently deletes a user account and associated records by ID. Enforces ROLE_ADMIN authorization."
	)
	@DeleteMapping("/{userId}")
	public ResponseEntity<Boolean> deleteUser(
			@Parameter(description = "ID of the user to delete", required = true)
			@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}
}
