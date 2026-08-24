package com.ecom.user.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.request.AddressRequest;
import com.ecom.user.response.AddressResponse;
import com.ecom.user.service.AddressService;
import com.ecom.user.util.CommonUtil;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
	
	private final AddressService addressService;
	
	@PostMapping
	public ResponseEntity<?> addAddressForUser(@RequestBody AddressRequest req) {
		AddressResponse address = addressService.addAddress(req);
		
		if(Objects.isNull(address))
			return ResponseEntity.badRequest().body("Unable to save this address.");
		
		return ResponseEntity.ok().body(address);
	}
	
	@GetMapping
	public ResponseEntity<?> getAddresses() {
		
		List<AddressResponse> allAddresses = addressService.getAllAddresses();
		
		if(CommonUtil.isEmpty(allAddresses))
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(allAddresses);
	}
	

}
