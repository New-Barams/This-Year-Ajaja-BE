package com.newbarams.ajaja.module.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.user.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.application.port.out.GetOauthProfilePort;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class LoginService implements LoginUseCase {
	private final AuthorizePort authorizePort;
	private final GetOauthProfilePort getOauthProfilePort;
	private final UserRepository userRepository;
	private final JwtGenerator jwtGenerator;

	@Override
	public UserResponse.Token login(String authorizationCode, String redirectUri) {
		AccessToken accessToken = authorizePort.authorize(authorizationCode, redirectUri);
		Profile profile = getOauthProfilePort.getProfile(accessToken.getContent());
		User user = findUserOrCreateIfNotExists(profile.getEmail(), profile.getOauthId());
		return jwtGenerator.login(user.getId());
	}

	private User findUserOrCreateIfNotExists(String email, Long oauthId) {
		return userRepository.findByEmail(email)
				.orElseGet(() -> createUser(email, oauthId));
	}

	private User createUser(String email, Long oauthId) {
		User user = User.init(email, oauthId);
		return userRepository.save(user);
	}
}
