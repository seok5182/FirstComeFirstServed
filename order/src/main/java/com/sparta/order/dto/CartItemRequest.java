package com.sparta.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
	@NotNull
	Long productId,
	@Min(value = 1, message = "최소 1개는 담아야 합니다.")
	int quantity
) {

}
