package com.newbarams.ajaja.module.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.FindUserIdByEmailPort;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class LoginService implements LoginUseCase {
	private final AuthorizePort authorizePort;
	private final FindUserIdByEmailPort findUserIdByEmailPort;
	private final CreateUserPort createUserPort;
	private final JwtGenerator jwtGenerator;

	@Override
	public AuthResponse.Token login(String authorizationCode, String redirectUri) {
		Profile profile = authorizePort.authorize(authorizationCode, redirectUri);
		Long userId = findIdOrCreateIfNotExists(profile.getEmail(), profile.getOauthId());
		return jwtGenerator.login(userId);
	}

	private Long findIdOrCreateIfNotExists(String email, Long oauthId) {
		return findUserIdByEmailPort.findUserIdByEmail(email)
			.orElseGet(() -> createUserPort.create(email, oauthId));
	}
}
