package com.ecom.user.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.entity.User;
import com.ecom.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> getCurrentUser() {
		Optional<User> user = userService.getCurrentUser();
		
		if (user.isEmpty())
			return ResponseEntity.noContent().build();

		return ResponseEntity.ok(user.get());
	}

}
