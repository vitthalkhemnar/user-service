package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Admin login credentials")
public record AdminLoginRequest(
		@Schema(description = "Admin username", example = "admin_user", requiredMode = Schema.RequiredMode.REQUIRED)
		String username, 

		@Schema(description = "Admin password", example = "SecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
		String password
) {
}
