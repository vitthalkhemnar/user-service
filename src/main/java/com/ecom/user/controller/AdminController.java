package com.ecom.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.UserResponse;
import com.ecom.user.dto.UserUpdate;
import com.ecom.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@PutMapping("/user")
	public ResponseEntity<?> updateUserById(@RequestBody UserUpdate req) {
		UserResponse userResponse = userService.updateUserById(req);
		return ResponseEntity.ok(userResponse);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}
}
