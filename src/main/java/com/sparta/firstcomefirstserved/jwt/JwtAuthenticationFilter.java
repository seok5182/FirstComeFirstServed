package com.sparta.firstcomefirstserved.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.firstcomefirstserved.aes256.Aes;
import com.sparta.firstcomefirstserved.dto.LoginRequest;
import com.sparta.firstcomefirstserved.security.UserDetailsImpl;
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

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Aes aes;
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(Aes aes, JwtUtil jwtUtil) {
		this.aes = aes;
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
		log.info("로그인 시도");
		try {
			LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),
				LoginRequest.class);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					aes.encrypt_AES(loginRequest.getUsername()),
					loginRequest.getPassword(),
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
		HttpServletResponse response, FilterChain chain, Authentication authResult)
		throws IOException, ServletException {
		log.info("로그인 성공 및 JWT 생성");
		String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

		String token = jwtUtil.createToken(email);
		jwtUtil.addJwtToCookie(token, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
		HttpServletResponse response, AuthenticationException failed)
		throws IOException, ServletException {
		log.info("로그인 실패");
		response.setStatus(401);
	}
}