package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request payload to verify Email OTP and login/register")
public record VerifyOtpRequest(
		@Schema(description = "Customer email address", example = "customer@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
		String email,

		@Schema(description = "6-digit OTP code received via email", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
		String otp
) {
}
