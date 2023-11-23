package com.newbarams.ajaja.module.user.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenRequest {
	private static final String KAKAO_GRANT_TYPE = "authorization_code";

	private final String grantType;
	private final String clientId;
	private final String redirectUri;
	private final String code;
	private final String clientSecret;

	public KakaoTokenRequest(String clientId, String redirectUri, String code, String clientSecret) {
		this.grantType = KAKAO_GRANT_TYPE;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.code = code;
		this.clientSecret = clientSecret;
	}
}
