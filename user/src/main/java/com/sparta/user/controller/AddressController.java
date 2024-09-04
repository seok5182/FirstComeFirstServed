package com.sparta.user.controller;

import com.sparta.user.dto.AddressResponse;
import com.sparta.user.entity.Address;
import com.sparta.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/address")
public class AddressController {

	private final AddressService addressService;

	@GetMapping("/{userId}")
	public ResponseEntity<AddressResponse> getAddress(@PathVariable Long userId) {
		Address address = addressService.getDefaultAddress(userId);
		return ResponseEntity.ok(AddressResponse.from(address));
	}
}
