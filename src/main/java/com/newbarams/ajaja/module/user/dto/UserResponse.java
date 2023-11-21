package com.newbarams.ajaja.module.user.dto;

import static com.newbarams.ajaja.module.user.dto.UserResponse.*;

public sealed interface UserResponse permits MyPage, Token {
	record Token(
		String accessToken,
		String refreshToken
	) implements UserResponse {
	}

	record MyPage(
		String nickname,
		String defaultEmail,
		String remindEmail,
		boolean isEmailVerified,
		String receiveType
	) implements UserResponse {
	}
}
