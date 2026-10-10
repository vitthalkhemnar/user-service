package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PhoneSignupRequest(
		String phone,
		String otp,
		String firstName,
		String lastName,
		String email
) {}
