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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.constants.AppConstants;
import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.AuthResponse;
import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.RegisterRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.VerifyOtpRequest;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.security.OtpAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final OtpService otpService;

	@Transactional
	public AuthResponse register(RegisterRequest req) {
		if (userRepo.existsByUsername(req.username())) {
			throw new IllegalArgumentException("Username already exists: " + req.username());
		}
		if (req.email() != null && userRepo.existsByEmail(req.email())) {
			throw new IllegalArgumentException("Email already exists: " + req.email());
		}

		User user = new User();
		user.setUsername(req.username());
		user.setPassword(passwordEncoder.encode(req.password()));
		user.setEmail(req.email());
		user.setPhone(req.phone());
		user.setFirstName(req.firstName());
		user.setLastName(req.lastName());
		user.setRoles(AppConstants.ROLE_USER);
		userRepo.save(user);

		UserDetails userDetails = userDetailsService.loadUserByUsername(req.username());

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);

		return new AuthResponse(token, req.username(), roles);
	}

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
		userRepo.findByEmail(normalizedEmail).ifPresent(user -> {
			if (user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN")) {
				throw new IllegalArgumentException("Admin accounts must log in through the Admin Portal.");
			}
		});

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

		return new AuthResponse(token, userDetails.getUsername(), roles);
	}
}
