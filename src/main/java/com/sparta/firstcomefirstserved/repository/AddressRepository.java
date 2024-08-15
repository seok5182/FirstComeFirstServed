package com.sparta.firstcomefirstserved.repository;

import com.sparta.firstcomefirstserved.entity.Address;
import com.sparta.firstcomefirstserved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findAddressByUserAndDefaultAddress(User user, boolean type);

}