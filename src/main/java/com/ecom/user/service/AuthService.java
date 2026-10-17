package com.ecom.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.constants.AppConstants;
import com.ecom.user.dto.AdminLoginRequest;
import com.ecom.user.dto.AuthResponse;
import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.SendOtpRequest;
import com.ecom.user.dto.SignupRequest;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.security.OtpAuthenticationToken;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepo;
	private final AuthenticationManager authenticationManager;
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
			SecurityContextHolder.clearContext();
			throw new BadCredentialsException("Access denied: Admin role required");
		}

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		
		Optional<User> user = userRepo.findByEmail(req.username().trim().toLowerCase());
		if (user.isEmpty()) {
			log.info("User not found. " + req.username());
			SecurityContextHolder.clearContext();
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new AuthResponse(token, user.get().getFirstName(), roles);
	}

	public Map<String, String> sendOtp(SendOtpRequest req) {
		String email = req.email();
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		String normalizedEmail = email.trim().toLowerCase();
		
		if (req.action().equalsIgnoreCase("login")) {
			try {
				User user = userRepo.findByEmail(normalizedEmail)
						.orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
				String roles = user.getRoles();
				boolean isAdmin = Arrays.asList(roles.split(",")).stream()
						.anyMatch(a -> a.equals("ROLE_" + AppConstants.ROLE_ADMIN));

				if (isAdmin) {
					log.error("ADMIN cannot logged in via OTP : " + normalizedEmail);
					return Map.of("status", "fail", "message", "Admin accounts must log in via the Admin Login page.");
				}
			} catch (Exception e) {
				log.error("User not found with this email : " + normalizedEmail);
				return Map.of("status", "fail", "message", "You don't have account yet, please signup.");
			}
		}
		
		otpService.generateAndSendOtp(normalizedEmail);
		return Map.of("status", "success", "message", "OTP sent successfully to " + email);
	}

	public AuthResponse login(LoginRequest req) {
		Authentication authentication = authenticationManager.authenticate(
				new OtpAuthenticationToken(req.email(), req.otp())
		);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		
		Optional<User> user = userRepo.findByEmail(req.email().trim().toLowerCase());
		if (user.isEmpty()) {
			log.info("User not found. " + req.email());
			SecurityContextHolder.clearContext();
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new AuthResponse(token, user.get().getFirstName(), roles);
	}

	@Transactional
	public AuthResponse register(SignupRequest req) {
		if(req.otp() == null || req.otp().length() != 6) {
			throw new IllegalArgumentException("Otp is not valid");
		}
		
		if (req.phone() == null || req.phone().isBlank()) {
			throw new IllegalArgumentException("Phone number is required");
		}
		String normalizedPhone = req.phone().trim();
		
		if (req.email() == null || req.email().isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}
		String normalizedEmail = req.email().trim();
		if (userRepo.existsByEmail(normalizedEmail)) {
			throw new IllegalArgumentException("Email already exists: " + req.email());
		}

		User user = new User();
		user.setPhone(normalizedPhone);
		user.setFirstName(req.firstName() != null && !req.firstName().isBlank() ? req.firstName().trim() : null);
		user.setLastName(req.lastName() != null && !req.lastName().isBlank() ? req.lastName().trim() : null);
		user.setEmail(normalizedEmail);
		user.setRoles(AppConstants.ROLE_USER);
		userRepo.save(user);
		
		return login(new LoginRequest(normalizedEmail, req.otp()));
	}
}
