package com.sparta.order.dto;

import lombok.Builder;

@Builder
public record UpdateProduct(
	String type,
	int quantity) {

	public static UpdateProduct of(String type, int quantity) {
		return UpdateProduct.builder()
			.type(type)
			.quantity(quantity)
			.build();
	}
}
