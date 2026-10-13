package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request payload to update user profile information")
public record UserUpdateRequest(
    @Schema(description = "User unique ID", example = "42")
    Long id,

    @Schema(description = "First name", example = "Jane")
    String firstName,

    @Schema(description = "Last name", example = "Smith")
    String lastName,

    @Schema(description = "Email address", example = "jane.smith@example.com")
    String email,

    @Schema(description = "Mobile phone number", example = "+919876543210")
    String phone,

    @Schema(description = "Admin status flag", example = "false")
    boolean isAdmin
) {}