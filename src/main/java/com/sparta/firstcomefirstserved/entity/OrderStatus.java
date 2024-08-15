package com.sparta.firstcomefirstserved.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
	ORDER("주문중"),
	IN_DELIVERY("배송중"),
	DELIVERY_COMPLETED("배송완료"),
	CANCELLATION("취소 완료"),
	RETURN_REQUEST("반품 신청"),
	RETURN_COMPLETED("반품 완료");

	private final String status;

	OrderStatus(String status) {
		this.status = status;
	}

}
