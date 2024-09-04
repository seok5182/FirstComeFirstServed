package com.sparta.order.dto;

public record ProductResponse(
	Long id,
	String name,
	String content,
	int price,
	int quantity,
	boolean isDeleted
) {

}
