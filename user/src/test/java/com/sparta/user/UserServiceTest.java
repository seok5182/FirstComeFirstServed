package com.sparta.user;

import com.sparta.user.entity.Address;
import com.sparta.user.entity.User;
import com.sparta.user.repository.AddressRepository;
import com.sparta.user.repository.UserRepository;
import com.sparta.user.util.Aes256;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private Aes256 aes;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
		List<User> users = new ArrayList<>();
		List<Address> addresses = new ArrayList<>();
		for (int i = 1; i <= 10000; i++) {
			String name = "test" + i;
			String email = name + "@test.com";
			users.add(User.of(
				aes.encrypt_AES(email),
				passwordEncoder.encode("1234"),
				aes.encrypt_AES(name),
				aes.encrypt_AES("01012341234")
			));

			addresses.add(Address.of(
				aes.encrypt_AES(name),
				"12345",
				aes.encrypt_AES("테스트 주소"),
				aes.encrypt_AES("테스트 상세주소"),
				aes.encrypt_AES("01012341234"),
				(long) i
			));

			System.out.println(i + " : " + users.getLast());
		}

		userRepository.saveAll(users);
		addressRepository.saveAll(addresses);

		System.out.println("completed");
	}
}
