package com.sparta.payment.dto;

public record PaymentRequest (
	Long orderId,
	Long userId,
	int totalPrice
) {
}
