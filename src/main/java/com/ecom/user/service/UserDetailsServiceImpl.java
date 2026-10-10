package com.ecom.user.service;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		User user = userRepository.findByPhone(identifier)
				.or(() -> userRepository.findByEmail(identifier))
				.orElseThrow(() -> new UsernameNotFoundException("No user found with phone or email: " + identifier));

		return buildUserDetails(user);
	}

	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		String normalizedEmail = email.trim().toLowerCase();
		User user = userRepository.findByEmail(normalizedEmail)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + normalizedEmail));
		return buildUserDetails(user);
	}

	private UserDetails buildUserDetails(User user) {
		String role = user.getRoles();
		String password = user.getPassword() != null ? user.getPassword() : "";
		String principal = user.getPhone() != null ? user.getPhone() : user.getEmail();

		UserBuilder builder = org.springframework.security.core.userdetails.User.builder()
				.username(principal)
				.password(password);

		if (AppConstants.ROLE_ADMIN.equals(role))
			builder.roles(AppConstants.ROLE_USER, AppConstants.ROLE_ADMIN);
		else
			builder.roles(AppConstants.ROLE_USER);

		return builder.build();
	}
}
