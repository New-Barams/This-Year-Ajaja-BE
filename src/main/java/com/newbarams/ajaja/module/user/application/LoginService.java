package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
	private final AuthorizeService authorizeService;
	private final GetProfileService getProfileService;
	private final UserRepository userRepository;
	private final JwtGenerator jwtGenerator;

	public UserResponse.Token login(String authorizationCode, String redirectUri) {
		AccessToken accessToken = authorizeService.authorize(authorizationCode, redirectUri);
		Profile profile = getProfileService.getProfile(accessToken.getContent());
		User user = findUserOrCreateIfNotExists(profile.getEmail(), profile.getOauthId());
		return jwtGenerator.generate(user.getId());
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
