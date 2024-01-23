package com.newbarams.ajaja.infra.feign.ncp.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.naver")
public class NaverCloudProperties {
	private final String serviceId;
	private final String accessKey;
	private final String secretKey;
}
