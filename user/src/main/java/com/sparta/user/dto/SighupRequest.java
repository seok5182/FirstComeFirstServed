package com.sparta.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SighupRequest(
	@Email
	@NotBlank(message = "이메일을 입력해주세요")
	String email,
	@NotBlank(message = "비밀번호를 입력해주세요")
	String password,
	@NotBlank(message = "이름을 입력해주세요")
	String name,
	@NotBlank(message = "전화번호를 입력해주세요")
	String tel,
	@NotBlank(message = "우편번호를 입력해주세요")
	@Size(min = 5, max = 5)
	String zipCode,
	@NotBlank(message = "주소를 입력해주세요")
	String addr1,
	@NotBlank(message = "상세주소를 입력해주세요")
	String addr2
) {

}
