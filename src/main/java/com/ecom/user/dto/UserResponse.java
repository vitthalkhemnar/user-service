package com.ecom.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User profile response details")
public record UserResponse(
    @Schema(description = "User unique ID", example = "42")
    Long id,

    @Schema(description = "Unique username", example = "john_doe")
    String username,

    @Schema(description = "Customer first name", example = "John")
    String firstName,

    @Schema(description = "Customer last name", example = "Doe")
    String lastName,

    @Schema(description = "Email address", example = "john.doe@example.com")
    String email,

    @Schema(description = "Mobile phone number", example = "+919876543210")
    String phone,

    @Schema(description = "Flag indicating whether user has admin privileges", example = "false")
    boolean isAdmin
) {}