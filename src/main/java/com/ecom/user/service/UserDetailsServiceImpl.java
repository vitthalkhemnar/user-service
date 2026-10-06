package com.ecom.user.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.user.constants.AppConstants;
import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.or(() -> userRepository.findByEmail(username))
				.orElseThrow(() -> new UsernameNotFoundException("No user found with username or email : " + username));

		return buildUserDetails(user);
	}

	@Transactional
	public UserDetails loadOrCreateUserByEmail(String email) {
		String normalizedEmail = email.trim().toLowerCase();
		User user = userRepository.findByEmail(normalizedEmail)
				.orElseGet(() -> {
					log.info("No user found with email [{}]. Creating new user account automatically.", normalizedEmail);
					User newUser = new User();
					newUser.setEmail(normalizedEmail);

					String baseUsername = normalizedEmail.contains("@")
							? normalizedEmail.substring(0, normalizedEmail.indexOf('@'))
							: normalizedEmail;

					String uniqueUsername = baseUsername;
					int count = 1;
					while (userRepository.existsByUsername(uniqueUsername)) {
						uniqueUsername = baseUsername + count++;
					}

					newUser.setUsername(uniqueUsername);
					newUser.setFirstName(baseUsername);
					newUser.setLastName(baseUsername);
					newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
					newUser.setRoles(AppConstants.ROLE_USER);

					User saved = userRepository.save(newUser);
					log.info("Created new user with id: {}, username: {}, email: {}", saved.getId(), saved.getUsername(), saved.getEmail());
					return saved;
				});

		return buildUserDetails(user);
	}

	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		return loadOrCreateUserByEmail(email);
	}

	private UserDetails buildUserDetails(User user) {
		String role = user.getRoles();
		String password = user.getPassword() != null ? user.getPassword() : "";

		UserBuilder builder = org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername() != null ? user.getUsername() : user.getEmail())
				.password(password);

		if (AppConstants.ROLE_ADMIN.equals(role))
			builder.roles(AppConstants.ROLE_USER, AppConstants.ROLE_ADMIN);
		else
			builder.roles(AppConstants.ROLE_USER);

		return builder.build();
	}
}
