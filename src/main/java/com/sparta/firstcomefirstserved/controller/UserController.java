package com.sparta.firstcomefirstserved.controller;

import com.sparta.firstcomefirstserved.dto.SignupRequest;
import com.sparta.firstcomefirstserved.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login-page")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/signup")
	public String signupPage() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid SignupRequest signupRequest, BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (!fieldErrors.isEmpty()) {
			for (FieldError fieldError : fieldErrors) {
				log.error(fieldError.getField() + "필드 : " + fieldError.getDefaultMessage());
			}
			return "redirect:/user/signup";
		}

		userService.signup(signupRequest);

		return "redirect:/user/login-page";

	}

}
