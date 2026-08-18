package com.ecom.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecom.user.entity.Address;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.request.LoginRequest;
import com.ecom.user.request.SignupRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	
	public User createUser(SignupRequest req) {
		
		User user = new User();
		
		user.setEmail(req.getEmail());
		user.setUsername(req.getUsername());
		user.setPhone(req.getPhone());
		user.setPassword(req.getPassword());
	
		return userRepo.save(user);
	}
	
	public Optional<User> userLogin(LoginRequest req) {
		
		String email = req.getEmail();
		String password = req.getPassword();
		
		return userRepo.findByEmailAndPassword(email, password);
	}
	
	public Optional<User> getUser(Long userId) {
		return userRepo.findById(userId);
	}
}