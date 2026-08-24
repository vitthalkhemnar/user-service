package com.ecom.user.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.request.LoginRequest;
import com.ecom.user.request.RegisterRequest;
import com.ecom.user.response.AuthResponse;

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
		userRepo.save(user);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(req.username());
		String token = jwtService.generateToken(userDetails);
		
		return new AuthResponse(token, req.username());
	}
	
	public AuthResponse login(LoginRequest req) {
		// Validates credentials using PasswordEncoder and UserDetailsService
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(req.username());
		String token = jwtService.generateToken(userDetails);
		
		return new AuthResponse(token, req.username());
	}
}
