package com.ecom.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "BookingService", url = "http://localhost:9092")
public interface BookingClient {

}
