package com.sparta.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
	IN_PROGRESS("주문진행중"),
	ORDER("주문완료"),
	IN_DELIVERY("배송중"),
	DELIVERY_COMPLETED("배송완료"),
	CANCELLATION("취소완료"),
	RETURN_REQUEST("반품신청"),
	RETURN_COMPLETED("반품완료");

	private final String status;

	OrderStatus(String status) {
		this.status = status;
	}

}