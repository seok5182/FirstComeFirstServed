package com.sparta.user.controller;

import com.sparta.user.dto.SighupRequest;
import com.sparta.user.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/signup")
public class SignupController {

	private final SignupService signupService;

	@PostMapping
	public ResponseEntity<Void> signup(@Valid @RequestBody SighupRequest request) {
		signupService.signup(request);
		return ResponseEntity.ok().build();
	}
}
