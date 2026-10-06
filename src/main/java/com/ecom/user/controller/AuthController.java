package com.ecom.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.RegisterRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.VerifyOtpRequest;
import com.ecom.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/admin/login")
	public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest req) {
		return ResponseEntity.ok(authService.adminLogin(req));
	}

	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest req) {
		return ResponseEntity.ok(authService.sendOtp(req));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
		return ResponseEntity.ok(authService.verifyOtp(req));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
		return ResponseEntity.ok(authService.register(req));
	}
}
