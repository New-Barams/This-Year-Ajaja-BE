package com.newbarams.ajaja.module.user.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public final class UserResponse {
	@Data
	public static class Token {
		private final String accessToken;
		private final String refreshToken;
	}

	@Data
	public static class MyPage {
		private final String nickname;
		private final String defaultEmail;
		private final String remindEmail;
		private final boolean isEmailVerified;
		private final String receiveType;

		@QueryProjection
		public MyPage(String nickname, String defaultEmail, String remindEmail, boolean isEmailVerified,
			String receiveType) {
			this.nickname = nickname;
			this.defaultEmail = defaultEmail;
			this.remindEmail = remindEmail;
			this.isEmailVerified = isEmailVerified;
			this.receiveType = receiveType;
		}
	}
}
