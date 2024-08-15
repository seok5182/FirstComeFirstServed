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

	@NotEmpty(message = "우편번호를 입력해주세요")
	private String zipCode;

	@NotEmpty(message = "주소를 입력해주세요")
	private String addr1;

	@NotEmpty(message = "상세주소를 입력해주세요")
	private String addr2;
}
