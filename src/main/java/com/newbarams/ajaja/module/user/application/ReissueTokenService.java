package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class ReissueTokenService implements ReissueTokenUseCase {
	private final JwtGenerator jwtGenerator;
	private final JwtValidator jwtValidator;

	@Override
	public AuthResponse.Token reissue(String accessToken, String refreshToken) {
		Long userId = jwtValidator.validateReissuableAndExtractId(accessToken, refreshToken);
		return jwtGenerator.reissue(userId, refreshToken);
	}
}
