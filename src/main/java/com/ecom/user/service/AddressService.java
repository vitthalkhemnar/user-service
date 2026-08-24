package com.ecom.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecom.user.entity.Address;
import com.ecom.user.entity.User;
import com.ecom.user.repository.AddressRepository;
import com.ecom.user.request.AddressRequest;
import com.ecom.user.response.AddressResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
	
	private final AddressRepository addressRepo;
	
	private final UserService userService;
	
	public AddressResponse addAddress(AddressRequest req) {
		Optional<User> userOpt = userService.getCurrentUser();
		
		if(userOpt.isEmpty()) {
			return null;
		}
		
		Address address = new Address();
		address.setHouseNo(req.houseNo());
		address.setStreet(req.street());
		address.setLandmark(req.landmark());
		address.setCity(req.city());
		address.setState(req.state());
		address.setCountry(req.country());
		address.setPincode(req.pincode());
		address.setUser(userOpt.get());
		
		Address savedAddress = addressRepo.save(address);
		
		return buildAddressResponse(savedAddress);
	}

	public List<AddressResponse> getAllAddresses() {
		Optional<User> userOpt = userService.getCurrentUser();

		if (userOpt.isEmpty()) {
			return null;
		}
		
		List<Address> addressList = addressRepo.findByUser(userOpt.get());
		return addressList.stream().map(a -> buildAddressResponse(a)).toList();
	}
	
	private AddressResponse buildAddressResponse(Address address) {
		return AddressResponse.builder()
				.username(address.getUser().getUsername())
				.houseNo(address.getHouseNo())
				.street(address.getStreet())
				.landmark(address.getLandmark())
				.city(address.getCity())
				.state(address.getState())
				.country(address.getCountry())
				.pincode(address.getPincode())
				.build();
	}
}
