package com.sparta.firstcomefirstserved.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "item")
@NoArgsConstructor
public class Item extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private String content;

	@Column(nullable = false)
	@Positive
	private int price;

	@Column(nullable = false)
	@PositiveOrZero
	private int quantity;

	@Column
	private boolean deleted;

	@OneToMany(mappedBy = "cartItem")
	private List<Cart> itemCartList = new ArrayList<>();

	@OneToMany(mappedBy = "orderItem")
	private List<OrderDetail> itemOrderList = new ArrayList<>();

	public Item(String name, String content, int price, int quantity) {
		this.name = name;
		this.content = content;
		this.price = price;
		this.quantity = quantity;
		this.deleted = false;
	}

}
