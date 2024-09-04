package com.sparta.user.service;

import com.sparta.user.entity.Address;
import com.sparta.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

	private final AddressRepository addressRepository;

	public void save(Address address) {
		addressRepository.save(address);
	}

	public Address getDefaultAddress(Long userId) {
		return addressRepository.findByUserIdAndIsDefaultTrue(userId);
	}
}
