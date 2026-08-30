package com.ecom.user.dto;

public record UserUpdate(
    Long id,
    String username,
    String firstName,
    String lastName,
    String email,
    String phone,
    boolean isAdmin
) {}