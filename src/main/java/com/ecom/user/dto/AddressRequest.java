package com.ecom.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Delivery address request details")
public record AddressRequest(
		@Schema(description = "Address ID (required for update, omitted for create)", example = "1")
		Long addressId,

		@Schema(description = "Building / Apartment / Suite name or number", example = "Flat 402, Sunshine Apartments", requiredMode = Schema.RequiredMode.REQUIRED)
        String building,

		@Schema(description = "Street / Area / Locality", example = "MG Road, Sector 4", requiredMode = Schema.RequiredMode.REQUIRED)
        String area,

		@Schema(description = "City", example = "Mumbai", requiredMode = Schema.RequiredMode.REQUIRED)
        String city,

		@Schema(description = "State / Province", example = "Maharashtra", requiredMode = Schema.RequiredMode.REQUIRED)
        String state,

		@Schema(description = "Country", example = "India", requiredMode = Schema.RequiredMode.REQUIRED)
        String country,

		@Schema(description = "Postal code / PIN code", example = "400001", requiredMode = Schema.RequiredMode.REQUIRED)
        String pincode
	) {
}
