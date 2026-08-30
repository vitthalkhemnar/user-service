package com.ecom.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ProductService", url = "http://localhost:9091")
public interface ProductClient {

	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts();
	
	@PostMapping(value = "/products/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file);
}
