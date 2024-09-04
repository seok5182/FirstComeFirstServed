package com.sparta.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private String content;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int quantity;

	@Column
	private boolean isDeleted;

	@CreatedDate
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;

	@Builder
	public Product(String name, String content, int price, int quantity, boolean isDeleted) {
		this.name = name;
		this.content = content;
		this.price = price;
		this.quantity = quantity;
		this.isDeleted = isDeleted;
	}

	public static Product of(String name, String content, int price, int quantity) {
		return Product.builder()
			.name(name)
			.content(content)
			.price(price)
			.quantity(quantity)
			.isDeleted(false)
			.build();
	}

	public void minusQuantity(int quantity) {
		this.quantity -= quantity;
	}

	public void plusQuantity(int quantity) {
		this.quantity += quantity;
	}
}