package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Saved delivery address response details")
public record AddressResponse(
		@Schema(description = "Address unique identifier", example = "1")
		Long addressId,

		@Schema(description = "Building / Apartment / Suite name or number", example = "Flat 402, Sunshine Apartments")
        String building,

		@Schema(description = "Street / Area / Locality", example = "MG Road, Sector 4")
        String area,

		@Schema(description = "City", example = "Mumbai")
        String city,

		@Schema(description = "State / Province", example = "Maharashtra")
        String state,

		@Schema(description = "Country", example = "India")
        String country,

		@Schema(description = "Postal code / PIN code", example = "400001")
        String pincode
	) {
}
