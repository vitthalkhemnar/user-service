package com.ecom.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.user.entity.Address;
import com.ecom.user.entity.User;


import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUser(User user);
	
	Optional<Address> findByAddressIdAndUser(Long addressId, User user);
	
	@Transactional
	void deleteByAddressIdAndUser(Long addressId, User user);
}
