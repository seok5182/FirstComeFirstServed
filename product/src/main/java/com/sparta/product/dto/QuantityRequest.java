package com.sparta.product.dto;

public record QuantityRequest(
	Long orderId,
	Long productId,
	int quantity
) {

}
