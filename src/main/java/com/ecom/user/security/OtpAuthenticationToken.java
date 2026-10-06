package com.ecom.user.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OtpAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private Object credentials;

	public OtpAuthenticationToken(String email, String otp) {
		super(java.util.Collections.emptyList());
		this.principal = email;
		this.credentials = otp;
		setAuthenticated(false);
	}

	public OtpAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = null;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}
}
