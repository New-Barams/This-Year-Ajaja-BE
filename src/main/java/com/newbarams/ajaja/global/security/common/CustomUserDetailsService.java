package com.newbarams.ajaja.global.security.common;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.port.out.RetrieveUserPort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final RetrieveUserPort retrieveUserPort;

	@Override
	public UserDetails loadUserByUsername(String id) {
		return retrieveUserPort.load(Long.valueOf(id))
			.map(this::toAdapter)
			.orElseThrow(() -> new AjajaException(USER_NOT_FOUND));
	}

	private UserAdapter toAdapter(User user) {
		return new UserAdapter(user.getId(), user.getOauthId());
	}
}
