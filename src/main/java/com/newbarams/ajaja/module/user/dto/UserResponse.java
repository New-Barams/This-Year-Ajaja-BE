package com.newbarams.ajaja.module.user.dto;

import static com.newbarams.ajaja.module.user.dto.UserResponse.*;

public sealed interface UserResponse permits Token {
	record Token(
		String accessToken,
		String refreshToken
	) implements UserResponse {
	}
}
