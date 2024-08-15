package com.sparta.firstcomefirstserved.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

	@NotBlank(message = "이메일을 입력해주세요")
	private String username;

	@NotBlank
	private String password;
}
