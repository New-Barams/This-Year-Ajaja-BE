package me.ajaja.infra.feign.kakao.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.ajaja.infra.feign.kakao.model.KakaoRequest;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;

@Disabled
@SpringBootTest
class KakaoUnlinkFeignClientTest {
	@Autowired
	private KakaoUnlinkFeignClient kakaoUnlinkFeignClient;
	@Autowired
	private KakaoProperties kakaoProperties;

	@Test
	void unlink_Success_WithRealApiCall() {
		// given
		Long targetId = -1L;
		KakaoRequest.Unlink request = new KakaoRequest.Unlink(targetId);

		// when
		KakaoResponse.Withdraw response =
			kakaoUnlinkFeignClient.unlink("KakaoAK " + kakaoProperties.getAdminKey(), request);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(targetId);
	}
}
