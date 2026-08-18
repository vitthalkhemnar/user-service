package com.ecom.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.user.entity.Address;
import java.util.List;
import com.ecom.user.entity.User;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUser(User user);
}
