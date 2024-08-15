package com.sparta.firstcomefirstserved.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Positive
	private int totalPrice;

	@Enumerated(EnumType.STRING)
	@Column
	private OrderStatus status;

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
	@Positive
	private int totalCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetailList = new ArrayList<>();

	public Order(User user, int totalCount, int totalPrice, Address address) {
		this.user = user;
		this.totalPrice = totalPrice;
		this.status = OrderStatus.ORDER;
		this.name = address.getName();
		this.zipcode = address.getZipCode();
		this.addr1 = address.getAddr1();
		this.addr2 = address.getAddr2();
		this.tel = address.getTel();
		this.totalCount = totalCount;
	}
}
