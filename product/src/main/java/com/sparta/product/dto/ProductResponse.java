package com.sparta.product.dto;

import com.sparta.product.entity.Product;
import lombok.Builder;

@Builder
public record ProductResponse(
	Long id,
	String name,
	String content,
	int price,
	int quantity,
	boolean isDeleted
) {

	public static ProductResponse from(Product product) {
		return ProductResponse.builder()
			.id(product.getId())
			.name(product.getName())
			.content(product.getContent())
			.price(product.getPrice())
			.quantity(product.getQuantity())
			.isDeleted(product.isDeleted())
			.build();
	}
}
