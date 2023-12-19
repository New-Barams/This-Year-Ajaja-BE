package com.newbarams.ajaja.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public final class AuthResponse {
	@Data
	public static class Token {
		private final String accessToken;
		private final String refreshToken;
		@Schema(description = "UNIX 시간을 사용한다.", example = "1703002695")
		private final long accessTokenExpireIn;
	}
}
