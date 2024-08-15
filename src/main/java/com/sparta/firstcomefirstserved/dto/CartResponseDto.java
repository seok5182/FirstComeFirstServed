package com.sparta.firstcomefirstserved.dto;

import com.sparta.firstcomefirstserved.entity.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponseDto {

	private Long id;
	private Long userId;
	private Long itemId;
	private String name;
	private String content;
	private int price;
	private int quantity;
	private boolean deleted;

	public CartResponseDto(Cart cart) {
		this.id = cart.getId();
		this.userId = cart.getUser().getId();
		this.itemId = cart.getCartItem().getId();
		this.name = cart.getCartItem().getName();
		this.content = cart.getCartItem().getContent();
		this.price = cart.getCartItem().getPrice();
		this.quantity = cart.getQuantity();
		this.deleted = cart.getCartItem().isDeleted();
	}
}
