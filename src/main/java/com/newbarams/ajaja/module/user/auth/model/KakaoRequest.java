package com.newbarams.ajaja.module.user.auth.model;

import static com.newbarams.ajaja.module.user.auth.model.KakaoRequest.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public sealed interface KakaoRequest permits Token {
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	record Token(
		String grantType,
		String clientId,
		String redirectUri,
		String code,
		String clientSecret
	) implements KakaoRequest {
	}
}
