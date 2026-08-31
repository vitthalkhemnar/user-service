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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> getUserProfile() {
		UserResponse user = userService.getUserProfile();
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/all-users")
	public ResponseEntity<?> getAllUsers() {
		List<UserResponse> user = userService.getAllUsers();
		return ResponseEntity.ok(user);
	}

	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest req) {
		UserResponse user = userService.updateUserById(req);
		return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}
}
