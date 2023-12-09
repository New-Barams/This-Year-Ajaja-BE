package com.newbarams.ajaja.module.user.infra;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OauthInfo {
	enum OauthProvider {
		KAKAO
	}

	private Long oauthId;

	@Enumerated(EnumType.STRING)
	private OauthProvider oauthProvider;

	public static OauthInfo kakao(Long oauthId) { // using on mapper
		return new OauthInfo(oauthId, OauthProvider.KAKAO);
	}
}
