package com.newbarams.ajaja.module.user.auth.application;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.module.user.application.LoginService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoLoginService implements LoginService {
	private static final String KAKAO_GRANT_TYPE = "authorization_code";

	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;

	@Override
	public String login(String authorizationCode) {
		return null;
	}
}
