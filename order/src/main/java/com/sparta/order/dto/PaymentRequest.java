package com.sparta.order.dto;

import com.sparta.order.entity.Order;
import lombok.Builder;

@Builder
public record PaymentRequest (
	Long orderId,
	Long userId,
	int totalPrice
) {
	public static PaymentRequest of (Order order) {
		return PaymentRequest.builder()
			.orderId(order.getId())
			.userId(order.getUserId())
			.totalPrice(order.getTotalPrice())
			.build();
	}
}
