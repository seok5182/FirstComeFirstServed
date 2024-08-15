package com.sparta.firstcomefirstserved.dto;

import com.sparta.firstcomefirstserved.entity.Order;
import com.sparta.firstcomefirstserved.entity.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponse {

	private Long id;
	private int totalPrice;
	private LocalDateTime orderDate;
	private OrderStatus status;
	private String name;
	private String zipCode;
	private String addr1;
	private String addr2;
	private String tel;
	private int totalCount;

	public OrderResponse(Order order) {
		this.id = order.getId();
		this.totalPrice = order.getTotalPrice();
		this.orderDate = order.getCreatedAt();
		this.status = order.getStatus();
		this.name = order.getName();
		this.zipCode = order.getZipcode();
		this.addr1 = order.getAddr1();
		this.addr2 = order.getAddr2();
		this.tel = order.getTel();
		this.totalCount = order.getTotalCount();
	}
}
