package com.ecom.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Authentication response containing JWT token, username, and user roles")
public record AuthResponse(
		@Schema(description = "JWT Bearer token for authenticating subsequent requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
		String token,

		@Schema(description = "Authenticated user's username", example = "john_doe")
		String username,

		@Schema(description = "List of assigned authorities / roles", example = "[\"ROLE_USER\"]")
		List<String> roles
	) {
}
