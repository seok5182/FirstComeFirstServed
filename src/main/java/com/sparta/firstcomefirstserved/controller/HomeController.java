package com.sparta.firstcomefirstserved.controller;

import com.sparta.firstcomefirstserved.aes256.Aes;
import com.sparta.firstcomefirstserved.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	Aes aes = new Aes();

	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		model.addAttribute("email", userDetails.getUsername());
		return "index";
	}
}
