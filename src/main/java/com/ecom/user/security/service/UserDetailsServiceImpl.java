package com.ecom.user.security.service;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecom.user.entity.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.util.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("No user found with username : " + username));
		
		String role = user.getRoles();
		
		UserBuilder builder = org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword());
		
		if(AppConstants.ROLE_ADMIN.equals(role))
			builder.roles(AppConstants.ROLE_USER, AppConstants.ROLE_ADMIN);
		else
			builder.roles(AppConstants.ROLE_USER);	

		return builder.build();
	}

}
