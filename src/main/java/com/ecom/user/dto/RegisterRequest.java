package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequest(
		String username, 
		String firstName,
		String lastName,
		String password, 
		String email, 
		String phone
	) {
}
