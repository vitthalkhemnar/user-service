package com.ecom.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.AuthResponse;
import com.ecom.user.dto.SignupRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.SendPhoneOtpRequest;
import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.VerifyPhoneOtpRequest;
import com.ecom.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Tag(name = "Authentication - Admin", description = "Administrative credentials-based login flow enforcing ROLE_ADMIN authority")
	@Operation(
		summary = "Admin login",
		description = "Authenticates an administrator using username and password. Validates ROLE_ADMIN authority and returns a JWT token with admin claims."
	)
	@PostMapping("/admin/login")
	public ResponseEntity<AuthResponse> adminLogin(@RequestBody AdminLoginRequest req) {
		return ResponseEntity.ok(authService.adminLogin(req));
	}

	@Tag(name = "Authentication - Email OTP", description = "Customer passwordless login & registration flow using Email and 6-digit OTP stored in Redis")
	@Operation(
		summary = "Send OTP to email",
		description = "Generates a 6-digit OTP, stores it in Redis with key = <email> and a 5-minute TTL, and dispatches the OTP email. Admin accounts are blocked from OTP authentication."
	)
	@PostMapping("/send-otp")
	public ResponseEntity<Map<String, String>> sendOtp(@RequestBody SendOtpRequest req) {
		return ResponseEntity.ok(authService.sendOtp(req));
	}

	@Tag(name = "Authentication - Email OTP", description = "Customer passwordless login & registration flow using Email and 6-digit OTP stored in Redis")
	@Operation(
		summary = "Verify email OTP and login/register",
		description = "Validates the 6-digit OTP against Redis. If no account exists for this email, creates a new user account automatically and issues a JWT token."
	)
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> verifyOtp(@RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req));
	}

	@Tag(name = "Authentication - Email OTP", description = "Customer email registration and authentication flow using Email OTP")
	@Operation(
		summary = "Register customer with phone number",
		description = "Verifies the Email OTP and registers a new customer account with their email and profile details."
	)
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody SignupRequest req) {
		return ResponseEntity.ok(authService.register(req));
	}
}
