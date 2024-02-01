package me.ajaja.infra.feign.kakao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import me.ajaja.module.auth.application.model.AccessToken;
import me.ajaja.module.auth.application.model.Profile;

public final class KakaoResponse {
	@Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Token implements AccessToken {
		private final String accessToken;

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

		@Override
		public String getContact() {
			return kakaoAccount.phoneNumber();
		}
	}

	@Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Withdraw {
		private final Long id;
	}
}
