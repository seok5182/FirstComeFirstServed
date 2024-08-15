package com.sparta.firstcomefirstserved.security;

import com.sparta.firstcomefirstserved.entity.User;
import com.sparta.firstcomefirstserved.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));

		return new UserDetailsImpl(user);
	}

	public UserDetails loadUserById(long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UsernameNotFoundException("Not Found User " + id));

		return new UserDetailsImpl(user);
	}
}