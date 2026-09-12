package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressResponse(
		Long addressId,
        String building,
        String area,
        String city,
        String state,
        String country,
        String pincode
	) {
}
