package com.newbarams.ajaja.module.user.dto;

import com.newbarams.ajaja.global.annotation.EnumType;
import com.newbarams.ajaja.module.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public final class UserRequest {
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

	@Data
	public static class Receive {
		@EnumType(enumClass = User.ReceiveType.class)
		@Schema(description = "변경할 인증번호 타입", allowableValues = {"kakao", "email", "both"})
		private final User.ReceiveType type;
	}
}
