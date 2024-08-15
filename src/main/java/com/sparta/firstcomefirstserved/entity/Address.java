package com.sparta.firstcomefirstserved.entity;

import com.sparta.firstcomefirstserved.dto.SignupRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "address")
@NoArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String addr1;

	@Column(nullable = false)
	private String addr2;

	@Column(nullable = false)
	private String tel;

	@Column(nullable = false)
	private boolean defaultAddress;

	public Address(User user, SignupRequest signupRequest) {
		this.user = user;
		this.name = signupRequest.getName();
		this.zipCode = signupRequest.getZipCode();
		this.addr1 = signupRequest.getAddr1();
		this.addr2 = signupRequest.getAddr2();
		this.tel = signupRequest.getTel();
		this.defaultAddress = true;
	}
}
