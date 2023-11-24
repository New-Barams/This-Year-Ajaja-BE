package com.newbarams.ajaja.module.user.kakao.model;

import static com.newbarams.ajaja.module.user.kakao.model.KakaoResponse.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;

public sealed interface KakaoResponse permits Token, UserInfo {
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
	@JsonIgnoreProperties(ignoreUnknown = true)
	record UserInfo(
		Long id,
		KakaoAccount kakaoAccount
	) implements KakaoResponse, Profile {
		@Override
		public String getEmail() {
			return kakaoAccount().email();
		}
	}
}
