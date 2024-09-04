package com.sparta.user.service;

import com.sparta.user.dto.SighupRequest;
import com.sparta.user.entity.Address;
import com.sparta.user.entity.User;
import com.sparta.user.repository.UserRepository;
import com.sparta.user.util.Aes256;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupService {

	private final UserRepository userRepository;
	private final AddressService addressService;
	private final Aes256 aes;
	private final PasswordEncoder passwordEncoder;

	public void signup(SighupRequest signupRequest) {
		String email = aes.encrypt_AES(signupRequest.email());
		String password = passwordEncoder.encode(signupRequest.password());
		String name = aes.encrypt_AES(signupRequest.name());
		String tel = aes.encrypt_AES(signupRequest.tel());
		String addr1 = aes.encrypt_AES(signupRequest.addr1());
		String addr2 = aes.encrypt_AES(signupRequest.addr2());

		// 회원 중복 확인
		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("중복된 이메일이 있습니다!");
		}

		// 유저 등록
		User user = User.of(email, password, name, tel);
		User savedUser = userRepository.save(user);
		// 주소 등록
		Address address = Address.of(name, signupRequest.zipCode(), addr1, addr2, tel,
			savedUser.getId());
		addressService.save(address);
	}
}
