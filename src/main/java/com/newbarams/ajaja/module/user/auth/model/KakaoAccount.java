package com.newbarams.ajaja.module.user.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(
	String email,
	boolean isEmailValid,
	boolean isEmailVerified
) {
}
