package com.sparta.order.dto;

import com.sparta.order.entity.OrderDetail;
import lombok.Builder;

@Builder
public record QuantityRequest (
	Long orderId,
	Long productId,
	int quantity
) {
	public static QuantityRequest from (OrderDetail orderDetail) {
		return QuantityRequest.builder()
			.orderId(orderDetail.getOrderId())
			.productId(orderDetail.getProductId())
			.quantity(orderDetail.getQuantity())
			.build();
	}
}
