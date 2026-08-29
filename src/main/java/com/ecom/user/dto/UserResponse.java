package com.ecom.user.dto;

public record UserResponse(
    Long id,
    String username,
	String firstName,
	String lastName,
    String email,
    String phone
) {}