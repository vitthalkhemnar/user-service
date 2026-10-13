package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request payload to verify Phone login OTP")
public record VerifyPhoneOtpRequest(
		@Schema(description = "Customer mobile phone number", example = "+919876543210", requiredMode = Schema.RequiredMode.REQUIRED)
		String phone,

		@Schema(description = "6-digit SMS OTP code", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
		String otp
) {}
