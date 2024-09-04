package com.sparta.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.user.dto.LoginRequest;
import com.sparta.user.util.Aes256;
import com.sparta.user.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Aes256 aes;
	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;

	public JwtAuthenticationFilter(Aes256 aes, JwtUtil jwtUtil, ObjectMapper objectMapper) {
		this.aes = aes;
		this.jwtUtil = jwtUtil;
		this.objectMapper = new ObjectMapper();
		setFilterProcessesUrl("/users/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		//log.info("로그인 시도");
		try {
			LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
				LoginRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					aes.encrypt_AES(loginRequest.email()),
					loginRequest.password(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
		HttpServletResponse response, FilterChain chain, Authentication authResult) {
		//log.info("로그인 성공 및 JWT 생성");
		Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUserId();

		String token = jwtUtil.createToken(userId);
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
		response.addHeader("X-USER-ID", userId.toString());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
		HttpServletResponse response, AuthenticationException failed)
		throws IOException, ServletException {
		//log.info("로그인 실패");
		response.setStatus(401);
	}
}
