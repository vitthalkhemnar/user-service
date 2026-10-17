package com.ecom.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.user.dto.AddressRequest;
import com.ecom.user.dto.AddressResponse;
import com.ecom.user.entity.Address;
import com.ecom.user.entity.User;
import com.ecom.user.repository.AddressRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
	
	private final AddressRepository addressRepo;
	private final UserService userService;
	
	public List<AddressResponse> addAddress(AddressRequest req) {
		User user = userService.getCurrentUser();
		
		Address address = new Address();
		if(req.addressId() != null) {
			address = addressRepo.findByAddressIdAndUser(req.addressId(), user).orElse(new Address());
		}
			
		address.setBuilding(req.building());
		address.setArea(req.area());
		address.setCity(req.city());
		address.setState(req.state());
		address.setCountry(req.country());
		address.setPincode(req.pincode());
		address.setUser(user);
		
		addressRepo.save(address);
		
		return addressRepo.findByUser(user)
				.stream().map(this::buildAddressResponse)
				.toList();
	}

	public List<AddressResponse> getAllAddresses() {
		User user = userService.getCurrentUser();

		List<Address> addressList = addressRepo.findByUser(user);
		return addressList.stream().map(this::buildAddressResponse).toList();
	}
	
	public List<AddressResponse> deleteAddress(Long addressId) {
		User user = userService.getCurrentUser();

		addressRepo.deleteByAddressIdAndUser(addressId, user);
		
		return addressRepo.findByUser(user)
				.stream().map(this::buildAddressResponse)
				.toList();
	}

	public AddressResponse getAddressById(Long addressId) {
		if (addressId == null) return null;
		
		User user = userService.getCurrentUser();
		Address address = addressRepo.findByAddressIdAndUser(addressId, user).orElse(null);
		return address != null ? buildAddressResponse(address) : null;
	}

	private AddressResponse buildAddressResponse(Address address) {
		return AddressResponse.builder()
				.addressId(address.getAddressId())
				.building(address.getBuilding())
				.area(address.getArea())
				.city(address.getCity())
				.state(address.getState())
				.country(address.getCountry())
				.pincode(address.getPincode())
				.build();
	}
}
