package com.ecom.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.constants.AppConstants;
import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.AuthResponse;
import com.ecom.user.dto.PhoneSignupRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.SendPhoneOtpRequest;
import com.ecom.user.dto.VerifyOtpRequest;
import com.ecom.user.dto.VerifyPhoneOtpRequest;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.security.OtpAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepo;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final OtpService otpService;

	public AuthResponse adminLogin(AdminLoginRequest req) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(req.username(), req.password())
		);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals(AppConstants.ROLE_ADMIN));

		if (!isAdmin) {
			throw new BadCredentialsException("Access denied: Admin role required");
		}

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);

		return new AuthResponse(token, userDetails.getUsername(), roles);
	}

	public Map<String, String> sendOtp(SendOtpRequest req) {
		String email = req.email();
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}

		String normalizedEmail = email.trim().toLowerCase();
		User user = userRepo.findByEmail(normalizedEmail)
				.orElseThrow(() -> new IllegalArgumentException("No account found with this email. Please sign up."));

		if (user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN")) {
			throw new IllegalArgumentException("Admin accounts must log in through the Admin Portal.");
		}

		otpService.generateAndSendOtp(normalizedEmail);

		return Map.of("message", "OTP sent successfully to " + email);
	}

	public AuthResponse verifyOtp(VerifyOtpRequest req) {
		Authentication authentication = authenticationManager.authenticate(
				new OtpAuthenticationToken(req.email(), req.otp())
		);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		boolean isAdmin = roles.stream()
				.anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ROLE_" + AppConstants.ROLE_ADMIN));
		
		if (isAdmin) {
		    throw new BadCredentialsException("Admin accounts must log in via the Admin Login page.");
		}
		
		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		User user = userRepo.findByEmail(req.email().trim().toLowerCase()).orElse(null);
		String displayName = (user != null && user.getFirstName() != null) ? user.getFirstName() : userDetails.getUsername();

		return new AuthResponse(token, displayName, roles);
	}

	public Map<String, String> sendPhoneOtp(SendPhoneOtpRequest req) {
		String phone = req.phone();
		if (phone == null || phone.isBlank()) {
			throw new IllegalArgumentException("Phone number cannot be empty");
		}
		String normalizedPhone = phone.trim();
		if (userRepo.existsByPhone(normalizedPhone)) {
			throw new IllegalArgumentException("An account with this phone number already exists. Please log in.");
		}

		String otp = otpService.generateAndSendPhoneOtp(normalizedPhone);
		return Map.of("message", "OTP sent successfully to " + phone, "otp", otp);
	}

	@Transactional
	public AuthResponse registerWithPhone(PhoneSignupRequest req) {
		if (req.phone() == null || req.phone().isBlank()) {
			throw new IllegalArgumentException("Phone number is required");
		}
		String normalizedPhone = req.phone().trim();
		if (userRepo.existsByPhone(normalizedPhone)) {
			throw new IllegalArgumentException("An account with this phone number already exists. Please log in.");
		}
		if (req.otp() == null || !otpService.validatePhoneOtp(normalizedPhone, req.otp())) {
			throw new BadCredentialsException("Invalid or expired OTP");
		}
		otpService.consumePhoneOtp(normalizedPhone, req.otp());

		String normalizedEmail = null;
		if (req.email() != null && !req.email().isBlank()) {
			normalizedEmail = req.email().trim().toLowerCase();
			if (userRepo.existsByEmail(normalizedEmail)) {
				throw new IllegalArgumentException("Email already exists: " + req.email());
			}
		}

		User user = new User();
		user.setPhone(normalizedPhone);
		user.setFirstName(req.firstName() != null && !req.firstName().isBlank() ? req.firstName().trim() : null);
		user.setLastName(req.lastName() != null && !req.lastName().isBlank() ? req.lastName().trim() : null);
		user.setEmail(normalizedEmail);
		user.setRoles(AppConstants.ROLE_USER);
		User saved = userRepo.save(user);

		UserDetails userDetails = userDetailsService.loadUserByUsername(normalizedPhone);
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		String displayName = saved.getFirstName() != null ? saved.getFirstName() : saved.getPhone();
		return new AuthResponse(token, displayName, roles);
	}

	public Map<String, String> sendPhoneLoginOtp(SendPhoneOtpRequest req) {
		String phone = req.phone();
		if (phone == null || phone.isBlank()) {
			throw new IllegalArgumentException("Phone number cannot be empty");
		}
		String normalizedPhone = phone.trim();
		User user = userRepo.findByPhone(normalizedPhone)
				.orElseThrow(() -> new IllegalArgumentException("No account found with this phone number. Please sign up."));

		if (user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN")) {
			throw new IllegalArgumentException("Admin accounts must log in through the Admin Portal.");
		}

		String otp = otpService.generateAndSendPhoneOtp(normalizedPhone);
		return Map.of("message", "OTP sent successfully to " + phone, "otp", otp);
	}

	public AuthResponse verifyPhoneLoginOtp(VerifyPhoneOtpRequest req) {
		if (req.phone() == null || req.phone().isBlank()) {
			throw new IllegalArgumentException("Phone number cannot be empty");
		}
		String normalizedPhone = req.phone().trim();
		User user = userRepo.findByPhone(normalizedPhone)
				.orElseThrow(() -> new IllegalArgumentException("No account found with this phone number. Please sign up."));

		if (user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN")) {
			throw new BadCredentialsException("Admin accounts must log in through the Admin Portal.");
		}

		if (req.otp() == null || !otpService.validatePhoneOtp(normalizedPhone, req.otp())) {
			throw new BadCredentialsException("Invalid or expired OTP");
		}
		otpService.consumePhoneOtp(normalizedPhone, req.otp());

		UserDetails userDetails = userDetailsService.loadUserByUsername(normalizedPhone);
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		String displayName = (user.getFirstName() != null && !user.getFirstName().isBlank())
				? user.getFirstName()
				: user.getPhone();

		return new AuthResponse(token, displayName, roles);
	}
}
