package me.ajaja.module.remind.adapter.out.infra;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.domain.Receiver;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.domain.Target;
import me.ajaja.module.remind.util.RemindExceptionHandler;

class SendAlimtalkAdapterTest extends MockTestSupport {
	@InjectMocks
	private SendAlimtalkAdapter sendAlimtalkAdapter;

	@Mock
	private NaverSendAlimtalkFeignClient feignClient;
	@Mock
	private NaverCloudProperties properties;
	@Mock
	private RemindExceptionHandler exceptionHandler;
	@Mock
	private FindRemindableTargetsPort findRemindableTargetsPort;
	private Remind remind;

	@BeforeEach
	void setUp() {
		Receiver receiver = new Receiver(1L, "KAKAO", "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);
	}

	@Test
	@DisplayName("유저에게 알림톡 메세지를 전송한다.")
	void send_Success_WithNoException() {
		// given
		NaverResponse.AlimTalk response = sut.giveMeBuilder(NaverResponse.AlimTalk.class)
			.set("statusCode", "200").sample();
		given(findRemindableTargetsPort.findAllRemindablePlansByType(anyString(), anyString(), any()))
			.willReturn(List.of(remind));

		// when , then
		Assertions.assertThatNoException().isThrownBy(() ->
			sendAlimtalkAdapter.send("MORNING", TimeValue.now()
			));
	}
}
