package me.ajaja.infra.feign.ncp;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverRequest;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.ajaja.application.SendAjajaRemindPort;
import me.ajaja.module.remind.application.port.out.SendRemindPort;

@Component
@RequiredArgsConstructor
class NaverSendRemindService implements SendRemindPort, SendAjajaRemindPort {
	private final NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	private final NaverCloudProperties naverCloudProperties;

	@Override
	public NaverResponse.AlimTalk send(NaverRequest.Alimtalk request) {
		return naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(), request);
	}
}
