package com.sparta.firstcomefirstserved.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

	@Email
	@NotEmpty(message = "이메일을 입력해주세요")
	private String email;

	@NotEmpty(message = "비밀번호를 입력해주세요")
	private String password;

	@NotEmpty(message = "이름을 입력해주세요")
	private String name;

	@NotEmpty(message = "전화번호를 입력해주세요")
	private String tel;
}
