package com.newbarams.ajaja.module.user.dto;

import static com.newbarams.ajaja.module.user.dto.UserRequest.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public sealed interface UserRequest permits Reissue, EmailVerification {
	record Reissue(
		@NotBlank(message = "액세스 토큰에 빈 값이 들어올 수 없습니다.")
		@Schema(description = "Access Token", example = "eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ")
		String accessToken,

		@NotBlank(message = "리프레시 토큰에 빈 값이 들어올 수 없습니다.")
		@Schema(description = "Refresh Token", example = "eyJhbGxMiJ9.eyJzWpvdyJ9.avFKonhbIIhEg8H1dycQkhQ")
		String refreshToken
	) implements UserRequest {
	}

	record EmailVerification(
		@Email
		@Schema(description = "인증 받을 이메일. 가입 시 사용된 이메일과 다를 수 있습니다.")
		String email
	) implements UserRequest {
	}
}
