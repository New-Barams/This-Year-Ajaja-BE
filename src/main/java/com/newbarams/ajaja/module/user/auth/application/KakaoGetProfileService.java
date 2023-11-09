package com.newbarams.ajaja.module.user.auth.application;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProfileFeignClient;
import com.newbarams.ajaja.module.user.application.GetProfileService;
import com.newbarams.ajaja.module.user.auth.model.Profile;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
class KakaoGetProfileService implements GetProfileService {
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public Profile getProfile(String accessToken) {
		Objects.requireNonNull(accessToken, "access token must be not null");
		return kakaoProfileFeignClient.getKakaoProfile(accessToken);
	}
}
