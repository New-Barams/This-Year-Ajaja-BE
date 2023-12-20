package com.newbarams.ajaja.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public final class AuthRequest {
	@Data
	public static class Login {
		@NotBlank(message = "인가 코드로 빈 값이 들어올 수 없습니다.")
		@Schema(description = "인가 코드", example = "3yZ1t-T6P0lmA51PDW0jJkKjyXazFBEKKwyoAAABi_tgGJZAPV-WDrAHcw")
		private final String authorizationCode;

		@NotBlank(message = "리다이렉트 URL로 빈 값이 들어올 수 없습니다.")
		@Schema(description = "리다이렉트 URL", example = "http://localhost:3000/oauth")
		private final String redirectUri;
	}

	@Data
	public static class Reissue {
		@NotBlank(message = "액세스 토큰에 빈 값이 들어올 수 없습니다.")
		@Schema(description = "Access Token", example = "eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ")
		private final String accessToken;

		@NotBlank(message = "리프레시 토큰에 빈 값이 들어올 수 없습니다.")
		@Schema(description = "Refresh Token", example = "eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ")
		private final String refreshToken;
	}
}
