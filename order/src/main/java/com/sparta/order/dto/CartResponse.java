package com.sparta.order.dto;

import com.sparta.order.entity.Cart;
import lombok.Builder;

@Builder
public record CartResponse(
	Long cartId,
	Long productId,
	String name,
	String content,
	int price,
	int quantity
) {

	public static CartResponse from(ProductResponse productResponse, Cart cart) {
		return CartResponse.builder()
			.cartId(cart.getId())
			.productId(productResponse.id())
			.name(productResponse.name())
			.content(productResponse.content())
			.price(productResponse.price())
			.quantity(cart.getQuantity())
			.build();
	}
}
