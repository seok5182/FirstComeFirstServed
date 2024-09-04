package com.sparta.order.entity;

import com.sparta.order.dto.CartItemRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	Long userId;

	@Column(nullable = false)
	Long productId;

	@Column(nullable = false)
	@Positive
	private int quantity;

	@Builder
	public Cart(Long userId, Long productId, int quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

	static public Cart of(Long userId, CartItemRequest cartItemRequest) {
		return Cart.builder()
			.userId(userId)
			.productId(cartItemRequest.productId())
			.quantity(cartItemRequest.quantity())
			.build();
	}

	public void update(int quantity) {
		this.quantity = quantity;
	}
}
