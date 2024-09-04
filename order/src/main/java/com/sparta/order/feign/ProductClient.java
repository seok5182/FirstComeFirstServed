package com.sparta.order.feign;

import com.sparta.order.dto.ProductResponse;
import com.sparta.order.dto.QuantityRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("/products/{productId}")
	ProductResponse getProduct(@PathVariable("productId") Long productId);

	@PostMapping("/products/decrease")
	void decreaseQuantity(@RequestBody List<QuantityRequest> quantityRequests);

	@PostMapping("/products/increase")
	void increaseQuantity(@RequestBody List<QuantityRequest> quantityRequests);
}
