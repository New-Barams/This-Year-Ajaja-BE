package com.newbarams.ajaja.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public final class AuthRequest {
	@Data
	public static class Login {
		@NotBlank(message = "인가 코드로 빈 값이 들어올 수 없습니다.")
		private final String authorizationCode;

		@NotBlank(message = "리다이렉트 URL로 빈 값이 들어올 수 없습니다.")
		private final String redirectUri;
	}

	@Data
	public static class Reissue {
		@NotBlank(message = "액세스 토큰에 빈 값이 들어올 수 없습니다.")
		private final String accessToken;

		@NotBlank(message = "리프레시 토큰에 빈 값이 들어올 수 없습니다.")
		private final String refreshToken;
	}
}
