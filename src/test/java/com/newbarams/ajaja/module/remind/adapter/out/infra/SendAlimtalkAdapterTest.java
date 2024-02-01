package com.newbarams.ajaja.module.remind.adapter.out.infra;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import com.newbarams.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import com.newbarams.ajaja.infra.feign.ncp.model.NaverResponse.AlimTalk;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import com.newbarams.ajaja.module.remind.domain.Receiver;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.Target;
import com.newbarams.ajaja.module.remind.util.RemindExceptionHandler;

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
	private FindRemindablePlanPort findRemindablePlanPort;
	private Remind remind;

	@BeforeEach
	void setUp() {
		Receiver receiver = new Receiver(1L, null, "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);
	}

	@Test
	@DisplayName("유저에게 알림톡 메세지를 전송한다.")
	void send_Success_WithNoException() {
		// given
		AlimTalk response = sut.giveMeBuilder(AlimTalk.class).set("statusCode", "200").sample();
		given(findRemindablePlanPort.findAllRemindablePlan(anyString(), anyString(), any()))
			.willReturn(List.of(remind));

		// when , then
		Assertions.assertThatNoException().isThrownBy(() ->
			sendAlimtalkAdapter.send("MORNING", TimeValue.now()
			));
	}
}
