package com.sparta.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 5, nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String addr1;

	@Column(nullable = false)
	private String addr2;

	@Column(nullable = false)
	private String tel;

	@Column(nullable = false)
	private boolean isDefault;

	@Column
	private Long userId;

	@Builder
	public Address(String name, String zipCode, String addr1, String addr2, String tel, boolean isDefault, Long userId) {
		this.name = name;
		this.zipCode = zipCode;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.tel = tel;
		this.isDefault = isDefault;
		this.userId = userId;
	}

	public static Address of(String name, String zipCode, String addr1, String addr2, String tel, Long userId) {
		return Address.builder()
			.name(name)
			.zipCode(zipCode)
			.addr1(addr1)
			.addr2(addr2)
			.tel(tel)
			.isDefault(true)
			.userId(userId)
			.build();
	}
}
