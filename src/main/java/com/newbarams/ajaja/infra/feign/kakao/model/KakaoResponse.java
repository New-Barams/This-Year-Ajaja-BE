package com.newbarams.ajaja.infra.feign.kakao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newbarams.ajaja.module.auth.application.model.AccessToken;
import com.newbarams.ajaja.module.auth.application.model.Profile;

import lombok.Data;

public final class KakaoResponse {
	@Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Token implements AccessToken {
		private final String accessToken;
		private final String tokenType;
		private final String refreshToken;
		private final int expiresIn;
		private final String scope;
		private final String refreshTokenExpiresIn;

		@Override
		public String getContent() {
			return accessToken;
		}
	}

	@Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserInfo implements Profile {
		private final Long id;
		private final KakaoAccount kakaoAccount;

		@Override
		public Long getOauthId() {
			return id;
		}

		@Override
		public String getEmail() {
			return kakaoAccount.email();
		}
	}
}
