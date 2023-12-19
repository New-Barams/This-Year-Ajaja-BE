package com.newbarams.ajaja.module.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.user.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.FindUserIdByEmailPort;
import com.newbarams.ajaja.module.user.application.port.out.GetOauthProfilePort;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class LoginService implements LoginUseCase {
	private final AuthorizePort authorizePort;
	private final GetOauthProfilePort getOauthProfilePort;
	private final FindUserIdByEmailPort findUserIdByEmailPort;
	private final CreateUserPort createUserPort;
	private final JwtGenerator jwtGenerator;

	@Override
	public UserResponse.Token login(String authorizationCode, String redirectUri) {
		AccessToken accessToken = authorizePort.authorize(authorizationCode, redirectUri);
		Profile profile = getOauthProfilePort.getProfile(accessToken.getContent());
		Long userId = findIdOrCreateIfNotExists(profile.getEmail(), profile.getOauthId());
		return jwtGenerator.login(userId);
	}

	private Long findIdOrCreateIfNotExists(String email, Long oauthId) {
		return findUserIdByEmailPort.findUserIdByEmail(email)
				.orElseGet(() -> createUserPort.create(User.init(email, oauthId)));
	}
}
