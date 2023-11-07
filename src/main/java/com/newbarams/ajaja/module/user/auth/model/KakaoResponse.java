package com.newbarams.ajaja.module.user.auth.model;

import static com.newbarams.ajaja.module.user.auth.model.KakaoResponse.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public sealed interface KakaoResponse permits Token, Profile {
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	record Token(
		String accessToken,
		String tokenType,
		String refreshToken,
		int expiresIn,
		String scope,
		String refreshTokenExpiresIn
	) implements KakaoResponse, AccessToken {
		@Override
		public String getContent() {
			return accessToken;
		}
	}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	record Profile(
		Long id,
		KakaoAccount kakaoAccount
	) implements KakaoResponse {
	}
}
