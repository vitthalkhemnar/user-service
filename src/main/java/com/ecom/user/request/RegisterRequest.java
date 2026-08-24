package com.ecom.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequest(
		String username, 
		String password, 
		String email, 
		String phone
	) {
}
