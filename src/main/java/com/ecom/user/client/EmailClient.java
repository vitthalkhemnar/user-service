package com.ecom.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecom.user.config.FeignClientConfig;
import com.ecom.user.dto.SendOtpEmailRequest;

@FeignClient(name = "email-service", url = "http://localhost:9093", configuration = FeignClientConfig.class)
public interface EmailClient {

	@PostMapping("/email/otp")
	ResponseEntity<?> sendOtpMail(@RequestBody SendOtpEmailRequest req);

}
