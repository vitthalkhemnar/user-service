package com.ecom.user.service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ecom.user.client.EmailClient;
import com.ecom.user.dto.SendOtpEmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

	private static final long OTP_EXPIRY_MINUTES = 5;
	private final SecureRandom secureRandom = new SecureRandom();

	private final StringRedisTemplate redisTemplate;
	private final EmailClient emailClient;

	public String generateAndSendOtp(String email) {
		String normalizedEmail = email.trim().toLowerCase();
		String otp = String.format("%06d", secureRandom.nextInt(1_000_000));

		// Key is email, TTL is 5 minutes
		redisTemplate.opsForValue().set(
				normalizedEmail,
				otp,
				OTP_EXPIRY_MINUTES,
				TimeUnit.MINUTES
		);

		log.info("Generated OTP for {}: {}", normalizedEmail, otp);

		try {
			emailClient.sendOtpMail(new SendOtpEmailRequest(normalizedEmail, otp));
			log.info("Successfully dispatched OTP email request to email-service for {}", normalizedEmail);
		} catch (Exception e) {
			log.warn("Failed to dispatch OTP to email-service for {}. Error: {}", normalizedEmail, e.getMessage());
		}

		return otp;
	}

	public boolean validateOtp(String email, String otp) {
		if (email == null || otp == null || otp.isBlank()) {
			return false;
		}

		String normalizedEmail = email.trim().toLowerCase();
		// Key is email to retrieve from Redis
		String storedOtp = redisTemplate.opsForValue().get(normalizedEmail);

		return storedOtp != null && storedOtp.equals(otp.trim());
	}

	public void consumeOtp(String email, String otp) {
		if (email != null) {
			String normalizedEmail = email.trim().toLowerCase();
			redisTemplate.delete(normalizedEmail);
		}
	}

	public String generateAndSendPhoneOtp(String phone) {
		String normalizedPhone = phone.trim();
		String otp = String.format("%06d", secureRandom.nextInt(1_000_000));

		redisTemplate.opsForValue().set(
				"OTP_PHONE:" + normalizedPhone,
				otp,
				OTP_EXPIRY_MINUTES,
				TimeUnit.MINUTES
		);

		log.info("Generated Phone OTP for {}: {}", normalizedPhone, otp);
		return otp;
	}

	public boolean validatePhoneOtp(String phone, String otp) {
		if (phone == null || otp == null || otp.isBlank()) {
			return false;
		}
		String storedOtp = redisTemplate.opsForValue().get("OTP_PHONE:" + phone.trim());
		return storedOtp != null && storedOtp.equals(otp.trim());
	}

	public void consumePhoneOtp(String phone, String otp) {
		if (phone != null) {
			redisTemplate.delete("OTP_PHONE:" + phone.trim());
		}
	}
}
