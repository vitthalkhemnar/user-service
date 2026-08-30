package com.ecom.user.security.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.user.dto.AuthResponse;
import com.ecom.user.dto.LoginRequest;
import com.ecom.user.dto.RegisterRequest;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.util.AppConstants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepo;
	private final PasswordEncoder passworEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	
	@Transactional
	public AuthResponse register(RegisterRequest req) {
		
		User user = new User();
		user.setUsername(req.username());
		user.setPassword(passworEncoder.encode(req.password()));
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
	
	public AuthResponse login(LoginRequest req) {
		// Validates credentials using PasswordEncoder and UserDetailsService
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(req.username());
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		
		String token = jwtService.generateToken(Map.of("role", roles), userDetails);
		
		return new AuthResponse(token, req.username(), roles);
	}
}
