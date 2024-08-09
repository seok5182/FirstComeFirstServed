package com.sparta.firstcomefirstserved.service;

import com.sparta.firstcomefirstserved.aes256.Aes;
import com.sparta.firstcomefirstserved.dto.SignupRequest;
import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final Aes aes;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, Aes aes, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.aes = aes;
		this.passwordEncoder = passwordEncoder;
	}

	public void signup(SignupRequest signupRequest) {
		String email = aes.encrypt_AES(signupRequest.getEmail());
		String password = passwordEncoder.encode(signupRequest.getPassword());
		String name = aes.encrypt_AES(signupRequest.getName());
		String tel = aes.encrypt_AES(signupRequest.getTel());

		// 회원 중복 확인
		Optional<User> checkEmail = userRepository.findByEmail(email);
		if (checkEmail.isPresent()) {
			throw new IllegalArgumentException("중복된 유저가 있습니다.");
		}

		// 유저 등록
		User user = new User(email, password, name, tel);
		userRepository.save(user);
	}

}
