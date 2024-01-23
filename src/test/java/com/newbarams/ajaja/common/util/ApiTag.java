package com.newbarams.ajaja.common.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiTag {
	AUTH("인증 API"),
	FEEDBACK("피드백 API"),
	PLAN("계획 API"),
	REMIND("리마인드 API"),
	USER("사용자 API");

	final String content;
}
