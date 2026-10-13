package com.ecom.user.controller;

import java.util.List;

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
import com.ecom.user.dto.AddressResponse;
import com.ecom.user.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Tag(name = "Address Management", description = "Endpoints for managing customer shipping and billing addresses")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

	private final AddressService addressService;

	@Operation(
		summary = "Add new delivery address",
		description = "Creates and attaches a new shipping address to the currently authenticated user's address book."
	)
	@PostMapping
	public ResponseEntity<List<AddressResponse>> addAddressForUser(@RequestBody AddressRequest req) {
		return ResponseEntity.ok().body(addressService.addAddress(req));
	}

	@Operation(
		summary = "Get all addresses of user",
		description = "Fetches the full list of saved delivery addresses belonging to the authenticated user."
	)
	@GetMapping
	public ResponseEntity<List<AddressResponse>> getAddresses() {
		return ResponseEntity.ok().body(addressService.getAllAddresses());
	}

	@Operation(
		summary = "Get address by ID",
		description = "Retrieves the details of a specific delivery address by addressId."
	)
	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponse> getAddressById(
			@Parameter(description = "ID of the target address", required = true)
			@PathVariable("addressId") Long addressId) {
		AddressResponse addr = addressService.getAddressById(addressId);
		if (addr == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(addr);
	}

	@Operation(
		summary = "Update address",
		description = "Updates an existing shipping address in the user's address book."
	)
	@PutMapping
	public ResponseEntity<List<AddressResponse>> updateAddress(@RequestBody AddressRequest req) {
		return ResponseEntity.ok(addressService.addAddress(req));
	}

	@Operation(
		summary = "Delete address by ID",
		description = "Removes a delivery address from the user's address book by its ID."
	)
	@DeleteMapping("/{addressId}")
	public ResponseEntity<List<AddressResponse>> deleteAddress(
			@Parameter(description = "ID of the address to delete", required = true)
			@PathVariable("addressId") Long addressId) {
		return ResponseEntity.ok(addressService.deleteAddress(addressId));
	}
}
