package com.sparta.order.feign;

import com.sparta.order.dto.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface AddressClient {

	@GetMapping("/users/address/{userId}")
	AddressResponse getAddress(@PathVariable("userId") Long userId);
}
