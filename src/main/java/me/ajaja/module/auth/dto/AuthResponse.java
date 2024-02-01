package me.ajaja.module.auth.dto;

import lombok.Data;

public final class AuthResponse {
	@Data
	public static class Token {
		private final String accessToken;
		private final String refreshToken;
		private final long accessTokenExpireIn;
	}
}
