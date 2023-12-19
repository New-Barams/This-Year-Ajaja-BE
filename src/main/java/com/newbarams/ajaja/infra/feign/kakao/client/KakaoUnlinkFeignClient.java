package com.newbarams.ajaja.infra.feign.kakao.client;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.newbarams.ajaja.infra.feign.kakao.model.KakaoUnlinkRequest;

@FeignClient(name = "KakaoDisconnectFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUnlinkFeignClient {
	@PostMapping("/v1/user/unlink")
	void unlink(@RequestHeader(AUTHORIZATION) String adminKey, @RequestBody KakaoUnlinkRequest request);
}
