package com.ecom.user.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.entity.User;
import com.ecom.user.request.LoginRequest;
import com.ecom.user.request.SignupRequest;
import com.ecom.user.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
		
		User user = userService.createUser(req);
		
		return ResponseEntity.ok(user);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
		
		Optional<User> userOpt = userService.userLogin(req);
		
		if(userOpt.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(userOpt.get());
	}
}
