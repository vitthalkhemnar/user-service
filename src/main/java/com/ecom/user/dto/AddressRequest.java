package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressRequest(
		Long addressId,
        String building,
        String area,
        String city,
        String state,
        String country,
        String pincode
	) {
}
