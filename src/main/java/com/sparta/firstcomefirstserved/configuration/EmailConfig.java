package com.sparta.firstcomefirstserved.configuration;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Value("${mail.sender.id}")
	private String id;

	@Value("${mail.sender.password}")
	private String pw;

	@Bean
	// JAVA MAILSENDER 인터페이스를 구현한 객체를 빈으로 등록하기 위함.
	public JavaMailSender mailSender() {


		// JavaMailSender의 구현체를 생성
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// 속성을 넣기 시작
		// 이메일 전송에 사용할 SMTP 서버 호스트를 설정
		mailSender.setHost("smtp.gmail.com");
		// 587로 포트를 지정
		mailSender.setPort(587);
		// 구글계정을 넣습니다.
		mailSender.setUsername(id);
		// 구글 앱 비밀번호를 넣습니다.
		mailSender.setPassword(pw);

		// JavaMail의 속성을 설정하기 위해 Properties 객체를 생성
		Properties javaMailProperties = new Properties();
		// 프로토콜로 smtp 사용
		javaMailProperties.put("mail.transport.protocol", "smtp");
		// smtp 서버에 인증이 필요
		javaMailProperties.put("mail.smtp.auth", "true");
		// SSL 소켓 팩토리 클래스 사용
		javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// STARTTLS(TLS를 시작하는 명령)를 사용하여 암호화된 통신을 활성화
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		// 디버깅 정보 출력
		javaMailProperties.put("mail.debug", "true");
		// smtp 서버의 ssl 인증서를 신뢰
		javaMailProperties.put("mail.smtp.ssl.trust", "smtp.google.com");
		//사용할 ssl 프로토콜 버젼
		javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

		//mailSender에 우리가 만든 properties 넣고
		mailSender.setJavaMailProperties(javaMailProperties);

		//빈으로 등록한다.
		return mailSender;
	}
}
