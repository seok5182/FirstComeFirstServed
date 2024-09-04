package com.sparta.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
	@NotBlank(message = "이름을 입력해주세요")
	String name,
	@NotBlank(message = "전화번호를 입력해주세요")
	String tel,
	@NotBlank(message = "우편번호를 입력해주세요")
	String zipCode,
	@NotBlank(message = "주소를 입력해주세요")
	String addr1,
	@NotBlank(message = "상세주소를 입력해주세요")
	String addr2
) {

}
