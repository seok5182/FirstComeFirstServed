package com.sparta.user.service;

import com.sparta.user.dto.LoginRequest;
import com.sparta.user.dto.TokenResponse;
import com.sparta.user.entity.User;
import com.sparta.user.repository.UserRepository;
import com.sparta.user.util.Aes256;
import com.sparta.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final Aes256 aes;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public TokenResponse login(LoginRequest loginRequest) {
		String email = aes.encrypt_AES(loginRequest.email());
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NullPointerException("해당하는 유저가 없습니다"));
		if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
		}

		String accessToken = jwtUtil.createToken(user.getId());

		return TokenResponse.of(accessToken);
	}
}
