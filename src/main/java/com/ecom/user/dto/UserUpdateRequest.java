package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdateRequest(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    boolean isAdmin
) {}