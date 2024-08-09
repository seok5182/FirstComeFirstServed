package com.sparta.firstcomefirstserved.configuration;

import com.sparta.firstcomefirstserved.aes256.Aes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

	// PasswordEncoder interface의 구현체가 BCryptPasswordEncoder임을 수동 빈 등록을 통해 명시
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Aes aes() {
		return new Aes();
	}
}
