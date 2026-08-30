package com.ecom.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthResponse(
		String token,
		String username,
		List<String> roles
	) {
}
