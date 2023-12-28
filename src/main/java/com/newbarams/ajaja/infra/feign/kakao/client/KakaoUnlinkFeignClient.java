package com.newbarams.ajaja.infra.feign.kakao.client;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoUnlinkRequest;

@FeignClient(name = "KakaoDisconnectFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUnlinkFeignClient {
	@PostMapping(value = "/v1/user/unlink", consumes = "application/x-www-form-urlencoded")
	KakaoResponse.Withdraw unlink(
		@RequestHeader(AUTHORIZATION) String adminKey,
		@RequestBody KakaoUnlinkRequest request
	);
}
