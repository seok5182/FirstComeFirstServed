package com.sparta.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {

	@PostMapping("/orders/payment/success")
	ResponseEntity<Void> handlePaymentSuccess(@RequestHeader(value = "X-USER-ID") Long userId, @RequestParam("orderId") Long orderId);
}
