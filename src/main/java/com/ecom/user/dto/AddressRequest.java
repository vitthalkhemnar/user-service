package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressRequest(
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
