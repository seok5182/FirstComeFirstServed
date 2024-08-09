package com.sparta.firstcomefirstserved.auth;

import com.sparta.firstcomefirstserved.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final JwtUtil jwtUtil;

	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/create-jwt")
	public String createJwt(HttpServletResponse res) {

		// Jwt 생성
		String token = jwtUtil.createToken("Robbie");

		// Jwt 쿠키 저장
		jwtUtil.addJwtToCookie(token, res);

		return "createJwt : " + token;
	}

	@GetMapping("/get-jwt")
	public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
		// JWT 토큰 substring
		String token = jwtUtil.substringToken(tokenValue);

		// 토큰 검증
		if(!jwtUtil.validateToken(token)){
			throw new IllegalArgumentException("Token Error");
		}

		// 토큰에서 사용자 정보 가져오기
		Claims info = jwtUtil.getUserInfoFromToken(token);
		// 사용자 username
		String userId = info.getSubject();
		System.out.println("userId = " + userId);
		// 사용자 권한
		String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
		System.out.println("authority = " + authority);

		return "getJwt : " + userId + ", " + authority;
	}
}
