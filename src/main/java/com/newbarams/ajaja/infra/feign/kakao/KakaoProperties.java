package com.newbarams.ajaja.infra.feign.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// todo: @ConstructorBinding
@Getter
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoProperties {
	@Value("${secret.kakao.client-id}")
	private String clientId;
	@Value("${secret.kakao.redirect-uri}")
	private String redirectUri;
	@Value("${secret.kakao.client-secret}")
	private String clientSecret;
}
