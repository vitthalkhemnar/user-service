package com.ecom.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.PhoneSignupRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.SendPhoneOtpRequest;
import com.ecom.user.dto.VerifyOtpRequest;
import com.ecom.user.dto.VerifyPhoneOtpRequest;
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

	@PostMapping("/send-phone-otp")
	public ResponseEntity<?> sendPhoneOtp(@RequestBody SendPhoneOtpRequest req) {
		return ResponseEntity.ok(authService.sendPhoneOtp(req));
	}

	@PostMapping("/register-phone")
	public ResponseEntity<?> registerWithPhone(@RequestBody PhoneSignupRequest req) {
		return ResponseEntity.ok(authService.registerWithPhone(req));
	}

	@PostMapping("/send-phone-login-otp")
	public ResponseEntity<?> sendPhoneLoginOtp(@RequestBody SendPhoneOtpRequest req) {
		return ResponseEntity.ok(authService.sendPhoneLoginOtp(req));
	}

	@PostMapping("/verify-phone-login-otp")
	public ResponseEntity<?> verifyPhoneLoginOtp(@RequestBody VerifyPhoneOtpRequest req) {
		return ResponseEntity.ok(authService.verifyPhoneLoginOtp(req));
	}
}
