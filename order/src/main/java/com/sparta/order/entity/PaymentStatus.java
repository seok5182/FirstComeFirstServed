package com.sparta.order.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
	IN_PROGRESS("결제진행중"),
	SUCCESS("결제성공"),
	FAIL("결제실패");

	private final String status;

	PaymentStatus(String status) {
		this.status = status;
	}
}
