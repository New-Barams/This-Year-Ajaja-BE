package com.newbarams.ajaja.module.user.kakao.application;

import static com.newbarams.ajaja.global.util.BearerTokenUtil.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProfileFeignClient;
import com.newbarams.ajaja.module.user.application.GetProfileService;
import com.newbarams.ajaja.module.user.application.model.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoGetProfileService implements GetProfileService {
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public Profile getProfile(String accessToken) {
		return kakaoProfileFeignClient.getKakaoProfile(toBearer(accessToken));
	}
}
