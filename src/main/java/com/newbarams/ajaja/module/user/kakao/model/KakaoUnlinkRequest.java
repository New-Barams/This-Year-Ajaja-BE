package com.newbarams.ajaja.module.user.kakao.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoUnlinkRequest {
	private static final String KAKAO_TARGET_ID_TYPE = "user_id";

	private String target_id_type;
	private Long target_id;

	public KakaoUnlinkRequest(Long targetId) {
		this(KAKAO_TARGET_ID_TYPE, targetId);
	}
}
