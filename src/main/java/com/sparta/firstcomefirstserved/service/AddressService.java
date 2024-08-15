package com.sparta.firstcomefirstserved.service;

import com.sparta.firstcomefirstserved.entity.Address;
import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final AddressRepository addressRepository;

	public Address getDefaultAddress(User user) {
		return addressRepository.findAddressByUserAndDefaultAddress(user, true);
	}
}
