package com.sparta.order.dto;

public record AddressResponse(
	String name,
	String zipCode,
	String addr1,
	String addr2,
	String tel
) {

}