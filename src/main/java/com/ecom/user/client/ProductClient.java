package com.ecom.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.user.config.FeignClientConfig;

@FeignClient(name = "ProductService", url = "http://localhost:9091", configuration = FeignClientConfig.class)
public interface ProductClient {

	@GetMapping("/product")
	public ResponseEntity<?> getAllProducts();
	
}
