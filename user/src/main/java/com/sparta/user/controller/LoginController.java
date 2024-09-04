package com.sparta.user.controller;//package com.sparta.user.controller;
//
//import com.sparta.user.dto.LoginRequest;
//import com.sparta.user.dto.TokenResponse;
//import com.sparta.user.service.LoginService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/users/login")
//public class LoginController {
//
//	private final LoginService loginService;
//
//	@PostMapping
//	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
//		log.info("로그인 시도: {}", loginRequest);
//		TokenResponse tokenResponse = loginService.login(loginRequest);
//		log.info("로그인 성공 및 jwt 발행");
//		return ResponseEntity.ok()
//			.header(HttpHeaders.AUTHORIZATION, tokenResponse.accessToken())
//			.body(tokenResponse);
//	}
//}
