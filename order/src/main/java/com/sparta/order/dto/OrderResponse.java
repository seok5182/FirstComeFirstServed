package com.sparta.order.dto;

import com.sparta.order.entity.Order;
import com.sparta.order.entity.OrderStatus;
import com.sparta.order.entity.PaymentStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record OrderResponse(
	Long id,
	LocalDateTime orderDate,
	PaymentStatus paymentStatus,
	OrderStatus orderStatus,
	String name,
	String zipCode,
	String addr1,
	String addr2,
	String tel,
	int totalQuantity,
	int totalPrice
) {
	public static OrderResponse from(Order order) {
		return OrderResponse.builder()
			.id(order.getId())
			.orderDate(order.getCreatedAt())
			.paymentStatus(order.getPaymentStatus())
			.orderStatus(order.getOrderStatus())
			.name(order.getName())
			.zipCode(order.getZipcode())
			.addr1(order.getAddr1())
			.addr2(order.getAddr2())
			.tel(order.getTel())
			.totalQuantity(order.getTotalQuantity())
			.totalPrice(order.getTotalPrice())
			.build();
	}
}
