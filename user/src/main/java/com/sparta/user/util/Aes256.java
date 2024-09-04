package com.sparta.user.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class Aes256 {

	//알고리즘
	// AES/CBC/PKCS5Padding -> AES, CBC operation mode, PKCS5 padding scheme 으로 초기화된 Cipher 객체
	public static String algorithms = "AES/CBC/PKCS5Padding";

	//키
	private final static String AESKey = "abcdefghabcdefghabcdefghabcdefgh"; //32byte

	// 초기화 벡터
	private final static String AESIv = "0123456789abcdef"; //16byte

	// AES 암호화
	public String encrypt_AES(String str){
		try {
			// 암호화 결과 값을 담을 변수
			String result;

			// 암호화/복호화 기능이 포함된 객체 생성
			Cipher cipher = Cipher.getInstance(algorithms);

			// 키로 비밀키 생성
			SecretKeySpec keySpec = new SecretKeySpec(AESKey.getBytes(), "AES");

			// iv 로 spec 생성
			// 매번 다른 IV를 생성하면 같은 평문이라도 다른 암호문을 생성할 수 있다.
			// 또한 IV는 암호를 복호화할 사람에게 미리 제공되어야 하고 키와 달리 공개되어도 상관없다
			IvParameterSpec ivParamSpec = new IvParameterSpec(AESIv.getBytes());

			// 암호화 적용
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

			// 암호화 실행
			// ID 암호화(인코딩 설정)
			byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
			// 암호화 인코딩 후 저장
			result = Base64.getEncoder().encodeToString(encrypted);

			return result;
		}

		catch (Exception e) {
			throw new RuntimeException("암호화 중 오류 발생하였습니다.");
		}

	}

	// AES 복호화
	public String decrypt_AES(String str){
		try {
			// 암호화/복호화 기능이 포함된 객체 생성
			Cipher cipher = Cipher.getInstance(algorithms);

			// 키로 비밀키 생성
			SecretKeySpec keySpec = new SecretKeySpec(AESKey.getBytes(), "AES");

			// iv 로 spec 생성
			IvParameterSpec ivParamSpec = new IvParameterSpec(AESIv.getBytes());
			// 매번 다른 IV를 생성하면 같은 평문이라도 다른 암호문을 생성할 수 있다.
			// 또한 IV는 암호를 복호화할 사람에게 미리 제공되어야 하고 키와 달리 공개되어도 상관없다

			// 암호화 적용
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

			//암호 해석
			byte[] decodedBytes = Base64.getDecoder().decode(str);
			byte[] decrypted = cipher.doFinal(decodedBytes);

			return new String(decrypted, StandardCharsets.UTF_8);
		}

		catch (Exception e) {
			throw new RuntimeException("복호화 중 오류 발생하였습니다.");
		}

	}
}
