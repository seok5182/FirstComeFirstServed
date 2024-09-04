package com.sparta.user.dto;

import com.sparta.user.entity.Address;
import lombok.Builder;

@Builder
public record AddressResponse(
	String name,
	String zipCode,
	String addr1,
	String addr2,
	String tel
) {
	public static AddressResponse from (Address address) {
		return AddressResponse.builder()
			.name(address.getName())
			.zipCode(address.getZipCode())
			.addr1(address.getAddr1())
			.addr2(address.getAddr2())
			.tel(address.getTel())
			.build();
	}
}
