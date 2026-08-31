package com.ecom.user.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.ecom.user.config.FeignClientConfig;

@FeignClient(name = "BookingService", url = "http://localhost:9092", configuration = FeignClientConfig.class)
public interface BookingClient {

}
