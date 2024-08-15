package com.sparta.firstcomefirstserved.security;

import com.sparta.firstcomefirstserved.aes256.Aes;
import com.sparta.firstcomefirstserved.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

	private final User user;
	private final Aes aes = new Aes();

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getUserId() {
		return user.getId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return aes.decrypt_AES(user.getEmail());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}