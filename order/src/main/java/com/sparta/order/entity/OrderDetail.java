package com.sparta.order.entity;

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
@Entity
@NoArgsConstructor
@Table(name = "orderDetail")
public class OrderDetail extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long orderId;

	@Column(nullable = false)
	private Long productId;

	@Column
	@Positive
	private int price;

	@Column
	@Positive
	private int quantity;

	@Builder
	public OrderDetail(Long orderId, Long productId, int price, int quantity) {
		this.orderId = orderId;
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
	}

	public static OrderDetail of(Long orderId, Long productId, int price, int quantity) {
		return OrderDetail.builder()
			.orderId(orderId)
			.productId(productId)
			.price(price)
			.quantity(quantity)
			.build();
	}

	public void updateOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
