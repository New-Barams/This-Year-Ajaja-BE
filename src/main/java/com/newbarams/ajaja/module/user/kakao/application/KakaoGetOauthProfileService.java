package com.newbarams.ajaja.module.user.kakao.application;

import static com.newbarams.ajaja.global.util.BearerTokenUtil.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProfileFeignClient;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.application.port.out.GetOauthProfilePort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoGetOauthProfileService implements GetOauthProfilePort {
	private final KakaoProfileFeignClient kakaoProfileFeignClient;

	@Override
	public Profile getProfile(String accessToken) {
		return kakaoProfileFeignClient.getKakaoProfile(toBearer(accessToken));
	}
}
