package com.newbarams.ajaja.module.user.application.service;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.user.application.port.in.ReissueTokenUseCase;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class ReissueTokenService implements ReissueTokenUseCase {
	private final JwtGenerator jwtGenerator;
	private final JwtValidator jwtValidator;

	@Override
	public UserResponse.Token reissue(String accessToken, String refreshToken) {
		Long userId = jwtValidator.validateReissuableAndExtractId(accessToken, refreshToken);
		return jwtGenerator.reissue(userId, refreshToken);
	}
}
