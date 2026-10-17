package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request payload to trigger an Email OTP")
public record SendOtpRequest(
		@Schema(description = "Customer email address to receive OTP", example = "customer@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
		String email,
		
		@Schema(description = "Action to perform", example = "login", requiredMode = Schema.RequiredMode.REQUIRED)
		String action
) {
}
