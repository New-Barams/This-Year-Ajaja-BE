package com.newbarams.ajaja.module.user.kakao.application;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProfileFeignClient;
import com.newbarams.ajaja.module.user.application.GetProfileService;
import com.newbarams.ajaja.module.user.application.model.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoGetProfileService implements GetProfileService {
	private static final String BEARER_PREFIX = "Bearer ";

	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public Profile getProfile(String accessToken) {
		Objects.requireNonNull(accessToken, "access token must be not null");
		return kakaoProfileFeignClient.getKakaoProfile(BEARER_PREFIX + accessToken);
	}
}
