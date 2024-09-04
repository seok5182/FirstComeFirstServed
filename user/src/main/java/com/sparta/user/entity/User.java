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
@Table(name = "users")
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String tel;

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	public User(String email, String password, String name, String tel) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.tel = tel;
		this.deleted = false;
	}

	public static User of(String email, String password, String name, String tel) {
		return User.builder()
			.email(email)
			.password(password)
			.name(name)
			.tel(tel)
			.build();
	}
}
