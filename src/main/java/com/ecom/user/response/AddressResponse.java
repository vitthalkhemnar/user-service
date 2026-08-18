package com.ecom.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {

	private String userId;

	private String houseNo;

	private String street;

	private String landmark;

	private String city;

	private String state;

	private String country;

	private String pincode;
}
