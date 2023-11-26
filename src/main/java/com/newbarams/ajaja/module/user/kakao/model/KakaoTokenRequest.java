package com.newbarams.ajaja.module.user.kakao.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoTokenRequest {
	private static final String KAKAO_GRANT_TYPE = "authorization_code";

	private String grant_type;
	private String client_id;
	private String redirect_uri;
	private String code;
	private String client_secret;

	public KakaoTokenRequest(String clientId, String redirectUri, String code, String clientSecret) {
		this(KAKAO_GRANT_TYPE, clientId, redirectUri, code, clientSecret);
	}
}
