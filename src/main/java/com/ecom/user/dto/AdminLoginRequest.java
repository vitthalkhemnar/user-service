package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdminLoginRequest(
		String username, 
		String password
) {
}
