package me.ajaja.infra.feign.kakao.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public final class KakaoRequest {
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Authorize {
		private static final String KAKAO_GRANT_TYPE = "authorization_code";

		private String grant_type;
		private String client_id;
		private String redirect_uri;
		private String code;
		private String client_secret;

		public Authorize(String clientId, String redirectUri, String code, String clientSecret) {
			this(KAKAO_GRANT_TYPE, clientId, redirectUri, code, clientSecret);
		}
	}

	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Unlink {
		private static final String KAKAO_TARGET_ID_TYPE = "user_id";

		private String target_id_type;
		private Long target_id;

		public Unlink(Long targetId) {
			this(KAKAO_TARGET_ID_TYPE, targetId);
		}
	}
}
