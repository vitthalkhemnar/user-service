package com.ecom.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.AddressRequest;
import com.ecom.user.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private final AddressService addressService;

	@PostMapping
	public ResponseEntity<?> addAddressForUser(@RequestBody AddressRequest req) {
		return ResponseEntity.ok().body(addressService.addAddress(req));
	}

	@GetMapping
	public ResponseEntity<?> getAddresses() {
		return ResponseEntity.ok().body(addressService.getAllAddresses());
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteAddress(@PathVariable("addressId") Long addressId) {
		return ResponseEntity.ok(addressService.deleteAddress(addressId));
	}
	
	@PutMapping
	public ResponseEntity<?> updateAddress(@RequestBody AddressRequest req) {
		return ResponseEntity.ok(addressService.addAddress(req));
	}
}
