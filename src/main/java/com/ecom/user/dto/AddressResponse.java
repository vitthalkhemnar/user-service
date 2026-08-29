package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressResponse(
        String username,
        String houseNo,
        String street,
        String landmark,
        String city,
        String state,
        String country,
        String pincode
	) {
}
