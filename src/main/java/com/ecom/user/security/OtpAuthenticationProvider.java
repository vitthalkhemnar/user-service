package com.ecom.user.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ecom.user.service.OtpService;
import com.ecom.user.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {

	private final OtpService otpService;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = (String) authentication.getPrincipal();
		String otp = (String) authentication.getCredentials();

		boolean isValid = otpService.validateOtp(email, otp);
		if (!isValid) {
			throw new BadCredentialsException("Invalid or expired OTP");
		}

		UserDetails userDetails = userDetailsService.loadUserByEmail(email);

		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
		if (isAdmin) {
			throw new BadCredentialsException("Admin accounts must log in through the Admin Portal.");
		}

		otpService.consumeOtp(email, otp);

		return new OtpAuthenticationToken(userDetails, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OtpAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
