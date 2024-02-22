package me.ajaja.infra.feign.kakao.client;

import static org.springframework.http.MediaType.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import me.ajaja.infra.feign.kakao.model.KakaoRequest;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;

@FeignClient(name = "KakaoAuthorizeFeignClient", url = "https://kauth.kakao.com")
public interface KakaoAuthorizeFeignClient {
	@PostMapping(value = "/oauth/token", consumes = APPLICATION_FORM_URLENCODED_VALUE)
	KakaoResponse.Token authorize(@RequestBody KakaoRequest.Authorize request);
}
