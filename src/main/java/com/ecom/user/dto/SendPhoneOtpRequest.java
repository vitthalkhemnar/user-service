package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request payload to trigger an SMS Phone OTP")
public record SendPhoneOtpRequest(
		@Schema(description = "Customer mobile phone number", example = "+919876543210", requiredMode = Schema.RequiredMode.REQUIRED)
		String phone
) {}
