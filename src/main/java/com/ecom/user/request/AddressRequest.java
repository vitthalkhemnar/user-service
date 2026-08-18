package com.ecom.user.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequest {

	private String userId;

	private String houseNo;

	private String street;

	private String landmark;

	private String city;

	private String state;

	private String country;

	private String pincode;
}
