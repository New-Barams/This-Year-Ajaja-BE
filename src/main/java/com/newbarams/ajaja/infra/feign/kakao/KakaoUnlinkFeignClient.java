package com.newbarams.ajaja.infra.feign.kakao;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoDisconnectFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUnlinkFeignClient {
	@PostMapping("/v1/user/unlink")
	void unlink(@RequestHeader(AUTHORIZATION) String accessToken);
}
