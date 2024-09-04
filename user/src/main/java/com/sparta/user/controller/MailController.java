package com.sparta.user.controller;

import com.sparta.user.dto.EmailCheckRequest;
import com.sparta.user.dto.EmailRequest;
import com.sparta.user.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {
	private final MailSendService mailService;
	@PostMapping("/send")
	public String mailSend(@RequestBody @Valid EmailRequest emailRequest) {
		log.info("이메일 인증 요청 들어옴");
		log.info("이메일 인증 이메일 : " + emailRequest.getEmail());
		return mailService.joinEmail(emailRequest.getEmail());
	}
	@PostMapping("/check")
	public String mailCheck(@RequestBody @Valid EmailCheckRequest emailCheckRequest) {
		boolean isChecked = mailService.checkAuthNum(emailCheckRequest.getEmail(), emailCheckRequest.getAuthNum());
		if(isChecked) {
			return "ok";
		} else {
			return "fail";
			//throw new NullPointerException("인증이 실패했습니다");
		}
	}
}
