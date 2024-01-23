package com.newbarams.ajaja.module.remind.adapter.out.infra;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import com.newbarams.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import com.newbarams.ajaja.infra.feign.ncp.model.NaverRequest.Alimtalk;
import com.newbarams.ajaja.module.remind.application.port.out.SendAlimtalkRemindPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SendAlimtalkRemindAdapter implements SendAlimtalkRemindPort {
	private final NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	private final NaverCloudProperties naverCloudProperties;

	@Override
	public void send(String to, String planName, String message, String feedbackUrl) {
		Alimtalk request = Alimtalk.remind(to, planName, message, feedbackUrl);
		naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(), request);
	}
}
