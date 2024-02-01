package me.ajaja.global.security.common;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.user.application.port.out.RetrieveUserPort;
import me.ajaja.module.user.domain.User;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final RetrieveUserPort retrieveUserPort;

	@Override
	public UserDetails loadUserByUsername(String id) {
		return retrieveUserPort.loadById(Long.valueOf(id))
			.map(this::toAdapter)
			.orElseThrow(() -> new AjajaException(USER_NOT_FOUND));
	}

	private UserAdapter toAdapter(User user) {
		return new UserAdapter(user.getId(), user.getOauthId());
	}
}
