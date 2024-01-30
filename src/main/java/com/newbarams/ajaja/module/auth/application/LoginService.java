package com.newbarams.ajaja.module.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.JwtGenerator;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.RetrieveUserPort;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class LoginService implements LoginUseCase {
	private final AuthorizePort authorizePort;
	private final CreateUserPort createUserPort;
	private final ApplyChangePort applyChangePort;
	private final RetrieveUserPort retrieveUserPort;
	private final JwtGenerator jwtGenerator;

	@Override
	public AuthResponse.Token login(String authorizationCode, String redirectUri) {
		Profile profile = authorizePort.authorize(authorizationCode, redirectUri);
		Long userId = findIdOrCreateIfNotExists(profile);
		return jwtGenerator.login(userId);
	}

	private Long findIdOrCreateIfNotExists(Profile profile) {
		return retrieveUserPort.loadByEmail(profile.getEmail())
			.map(user -> upToDateContact(user, profile.getContact()))
			.orElseGet(() -> createUserPort.create(
				User.init(profile.getOauthId(), profile.getContact(), profile.getEmail()))
			);
	}

	private Long upToDateContact(User user, String contact) {
		user.upToDateNumber(contact);
		applyChangePort.apply(user);
		return user.getId();
	}
}
