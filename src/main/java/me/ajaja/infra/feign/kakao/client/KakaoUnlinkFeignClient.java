package me.ajaja.infra.feign.kakao.client;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import me.ajaja.infra.feign.kakao.model.KakaoRequest;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;

@FeignClient(name = "KakaoDisconnectFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUnlinkFeignClient {

	@PostMapping(value = "/v1/user/unlink", consumes = APPLICATION_FORM_URLENCODED_VALUE)
	KakaoResponse.Withdraw unlink(
		@RequestHeader(AUTHORIZATION) String adminKey,
		@RequestBody KakaoRequest.Unlink request
	);
}
