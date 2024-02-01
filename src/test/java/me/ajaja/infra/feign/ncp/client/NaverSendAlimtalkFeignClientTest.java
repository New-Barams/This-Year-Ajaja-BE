package me.ajaja.infra.feign.ncp.client;

import static me.ajaja.infra.feign.ncp.model.NaverRequest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.ajaja.infra.feign.ncp.model.NaverResponse;

@Disabled
@SpringBootTest
class NaverSendAlimtalkFeignClientTest {
	@Autowired
	private NaverSendAlimtalkFeignClient naverSendAlimtalkFeignClient;
	@Autowired
	private NaverCloudProperties naverCloudProperties;

	@Test
	void send_Success_WithRealApiCall() {
		// given
		Alimtalk request = Alimtalk.remind("01012345678", "테스트임당", "안녕하세요?", "https://www.ajaja.me/plans/1");

		// when
		NaverResponse.AlimTalk response =
			naverSendAlimtalkFeignClient.send(naverCloudProperties.getServiceId(), request);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getMessages()).isNotEmpty().hasSize(1);
	}
}
