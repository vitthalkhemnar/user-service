package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SendOtpEmailRequest(
		String to,
		String otp
) {
}
