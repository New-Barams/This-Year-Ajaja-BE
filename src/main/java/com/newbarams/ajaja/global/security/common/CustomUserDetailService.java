package com.newbarams.ajaja.global.security.common;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.error.ErrorCode;
import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String id) {
		return userRepository.findById(Long.valueOf(id))
			.map(this::toAdapter)
			.orElseThrow(() -> new AjajaException(ErrorCode.USER_NOT_FOUND));
	}

	private UserAdapter toAdapter(User user) {
		return new UserAdapter(user.getId(), user.defaultEmail());
	}
}
