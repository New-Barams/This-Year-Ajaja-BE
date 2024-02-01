package me.ajaja.module.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import me.ajaja.module.user.domain.User;

public final class UserRequest {
	@Data
	public static class EmailVerification {
		@Email(message = "유효한 이메일 형식이 아닙니다.")
		private final String email;
	}

	@Data
	public static class Certification {
		@Pattern(regexp = "^\\d{6}$", message = "인증 번호는 6자리 숫자로 이루어져 있습니다.")
		private final String certification;
	}

	@Data
	public static class Receive {
		private final User.RemindType type;
	}
}
