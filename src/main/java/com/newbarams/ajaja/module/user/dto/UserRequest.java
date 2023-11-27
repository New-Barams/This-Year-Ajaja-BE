package com.newbarams.ajaja.module.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public final class UserRequest {
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

	@Data
	public static class EmailVerification {
		@Email(message = "유효한 이메일 형식이 아닙니다.")
		@Schema(description = "인증 받을 이메일. 가입 시 사용된 이메일과 다를 수 있습니다.")
		private final String email;
	}

	@Data
	public static class Certification {
		@Pattern(regexp = "^\\d{6}$", message = "인증 번호는 6자리 숫자로 이루어져 있습니다.")
		@Schema(description = "이메일 인증을 위해서 발급된 6자리 인증 번호")
		private final String certification;
	}
}
