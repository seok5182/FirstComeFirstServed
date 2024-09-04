package com.sparta.order.dto;

import com.sparta.order.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
	@NotNull
	Long orderId,
	@NotBlank
	OrderStatus orderStatus
) {

}
