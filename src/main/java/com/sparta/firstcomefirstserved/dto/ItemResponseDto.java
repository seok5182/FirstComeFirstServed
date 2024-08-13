package com.sparta.firstcomefirstserved.dto;

import com.sparta.firstcomefirstserved.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponseDto {

	private Long id;
	private String name;
	private String content;
	private int price;
	private int quantity;
	private boolean deleted;

	public ItemResponseDto(Item item) {
		this.id = item.getId();
		this.name = item.getName();
		this.content = item.getContent();
		this.price = item.getPrice();
		this.quantity = item.getQuantity();
		this.deleted = item.isDeleted();
	}
}
