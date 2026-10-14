package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Customer registration request using mobile phone and SMS OTP")
public record SignupRequest(
		@Schema(description = "Customer mobile phone number", example = "+919876543210", requiredMode = Schema.RequiredMode.REQUIRED)
		String phone,

		@Schema(description = "SMS verification code received on the mobile phone", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
		String otp,

		@Schema(description = "Customer first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
		String firstName,

		@Schema(description = "Customer last name", example = "Doe")
		String lastName,

		@Schema(description = "Optional email address associated with the account", example = "john.doe@example.com")
		String email
) {}
