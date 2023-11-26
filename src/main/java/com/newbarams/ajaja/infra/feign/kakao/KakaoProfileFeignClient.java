package com.newbarams.ajaja.infra.feign.kakao;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.newbarams.ajaja.module.user.kakao.model.KakaoResponse;

@FeignClient(name = "KakaoProfileFeignClient", url = "https://kapi.kakao.com")
public interface KakaoProfileFeignClient {
	@GetMapping("/v2/user/me")
	KakaoResponse.UserInfo getKakaoProfile(@RequestHeader(AUTHORIZATION) String accessToken);
}
