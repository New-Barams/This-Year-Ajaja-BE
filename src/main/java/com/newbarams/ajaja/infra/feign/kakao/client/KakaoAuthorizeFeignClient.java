package com.newbarams.ajaja.infra.feign.kakao.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoTokenRequest;

@FeignClient(name = "KakaoAuthorizeFeignClient", url = "https://kauth.kakao.com")
public interface KakaoAuthorizeFeignClient {
	@PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
	KakaoResponse.Token authorize(@RequestBody KakaoTokenRequest tokenRequest);
}
