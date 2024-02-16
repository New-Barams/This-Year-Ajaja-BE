package me.ajaja.module.auth.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.jwt.JwtGenerator;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
import me.ajaja.module.auth.dto.AuthResponse;

@Service
@RequiredArgsConstructor
class ReissueTokenService implements ReissueTokenUseCase {
	private final JwtGenerator jwtGenerator;
	private final JwtParser jwtParser;

	@Override
	public AuthResponse.Token reissue(String accessToken, String refreshToken) {
		Long userId = jwtParser.parseIdIfReissueable(accessToken, refreshToken);
		return jwtGenerator.reissue(userId, refreshToken);
	}
}
