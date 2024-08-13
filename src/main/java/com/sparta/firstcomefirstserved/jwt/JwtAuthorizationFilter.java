package com.sparta.firstcomefirstserved.jwt;

import com.sparta.firstcomefirstserved.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
		FilterChain filterChain) throws ServletException, IOException {

		//String tokenValue = jwtUtil.getTokenFromRequest(req);
		String tokenValue = jwtUtil.getJwtFromHeader(req);

		if (StringUtils.hasText(tokenValue)) {
			// JWT 토큰 substring
			//tokenValue = jwtUtil.substringToken(tokenValue);
			//log.info(tokenValue);

			if (!jwtUtil.validateToken(tokenValue)) {
				log.error("Token Error");
				return;
			}

			Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

			try {
				setAuthentication(Long.valueOf(info.getSubject()));
			} catch (Exception e) {
				log.error(e.getMessage());
				return;
			}
		}

		filterChain.doFilter(req, res);
	}

	// 인증 처리
	public void setAuthentication(Long userId) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(userId);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(Long userId) {

		UserDetails userDetails = userDetailsService.loadUserById(userId);
		return new UsernamePasswordAuthenticationToken(userDetails, null,
			userDetails.getAuthorities());
	}
}