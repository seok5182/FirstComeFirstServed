package com.sparta.order.entity;

import com.sparta.order.dto.AddressResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends TimeStamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column
	private PaymentStatus paymentStatus;

	@Enumerated(EnumType.STRING)
	@Column
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String zipcode;

	@Column(nullable = false)
	private String addr1;

	@Column(nullable = false)
	private String addr2;

	@Column(nullable = false)
	private String tel;

	@Column
	private int totalQuantity;

	@Column
	private int totalPrice;

	@Column(nullable = false)
	private Long userId;

	@Builder
	public Order(PaymentStatus paymentStatus, OrderStatus orderStatus, String name, String zipcode, String addr1, String addr2,
		String tel, int totalQuantity, int totalPrice, Long userId) {
		this.paymentStatus = paymentStatus;
		this.orderStatus = orderStatus;
		this.name = name;
		this.zipcode = zipcode;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.tel = tel;
		this.totalQuantity = totalQuantity;
		this.totalPrice = totalPrice;
		this.userId = userId;
	}

	public static Order of(Long userId, AddressResponse address) {
		return Order.builder()
			.paymentStatus(PaymentStatus.IN_PROGRESS)
			.orderStatus(OrderStatus.IN_PROGRESS)
			.name(address.name())
			.zipcode(address.zipCode())
			.addr1(address.addr1())
			.addr2(address.addr2())
			.tel(address.tel())
			.totalQuantity(0)
			.totalPrice(0)
			.userId(userId)
			.build();
	}

	public void updateOrder(int totalQuantity, int totalPrice) {
		this.totalQuantity = totalQuantity;
		this.totalPrice = totalPrice;
	}

	public void updateOrderStatus(OrderStatus status) {
		this.orderStatus = status;
	}

	public void updatePaymentStatus(PaymentStatus status) {
		this.paymentStatus = status;
	}
}
