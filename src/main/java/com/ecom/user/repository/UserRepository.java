package com.ecom.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByPhone(String phone);
	
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(String phone);
}
