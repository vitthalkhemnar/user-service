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
		String userId = req.getUserId();
		Optional<User> userOpt = userService.getUser(Long.valueOf(userId));
		
		if(userOpt.isEmpty()) {
			log.info("User not found for userId: {}", userId);
			return null;
		}
		
		Address address = new Address();
		address.setHouseNo(req.getHouseNo());
		address.setStreet(req.getStreet());
		address.setLandmark(req.getLandmark());
		address.setCity(req.getCity());
		address.setState(req.getState());
		address.setCountry(req.getCountry());
		address.setPincode(req.getPincode());
		address.setUser(userOpt.get());
		
		Address savedAddress = addressRepo.save(address);
		
		return buildAddressResponse(savedAddress);
	}

	public List<AddressResponse> getAllAddresses(String userId) {
		Optional<User> userOpt = userService.getUser(Long.valueOf(userId));

		if (userOpt.isEmpty()) {
			log.info("User not found for userId: {}", userId);
			return null;
		}
		
		List<Address> addressList = addressRepo.findByUser(userOpt.get());
		
		return addressList.stream().map(a -> buildAddressResponse(a)).toList();
	}
	
	private AddressResponse buildAddressResponse(Address address) {
		return AddressResponse.builder()
				.userId(address.getUser().getId().toString())
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
