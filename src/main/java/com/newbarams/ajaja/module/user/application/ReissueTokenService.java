package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReissueTokenService {
	private final JwtGenerator jwtGenerator;
	private final JwtValidator jwtValidator;

	public UserResponse.Token reissue(Long userId, String accessToken, String refreshToken) {
		jwtValidator.validate(userId, accessToken, refreshToken);
		return jwtGenerator.generate(userId);
	}
}
