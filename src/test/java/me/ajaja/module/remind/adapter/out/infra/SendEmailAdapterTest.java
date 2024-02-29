package me.ajaja.module.remind.adapter.out.infra;

import static java.lang.Thread.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.infra.ses.SesSendPlanRemindService;
import me.ajaja.module.remind.application.CreateRemindService;
import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
import me.ajaja.module.remind.domain.Receiver;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.domain.Target;

class SendEmailAdapterTest extends MockTestSupport {

	@InjectMocks
	private SendEmailAdapter sendEmailAdapter;

	@Mock
	private SesSendPlanRemindService sesSendPlanRemindService;
	@Mock
	private FindRemindableTargetsPort findRemindableTargetsPort;
	@Mock
	private CreateRemindService createRemindService;

	@Test
	@DisplayName("아마존 ses를 통해 리마인드 될 정보들을 전송한다.")
	void send_Success_WithNoException() throws InterruptedException {
		// given
		Receiver receiver = new Receiver(1L, "KAKAO", "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);

		given(findRemindableTargetsPort.findAllRemindablePlansByType(anyString(), anyString(), any()))
			.willReturn(List.of(remind));
		given(sesSendPlanRemindService.send(anyString(), anyString(), anyString(), anyString())).willReturn(202);

		// when
		sendEmailAdapter.send("MORNING", TimeValue.now());
		sleep(100);

		// then
		then(sesSendPlanRemindService).should(times(1))
			.send(anyString(), anyString(), anyString(), anyString());
	}

	@ParameterizedTest
	@ValueSource(ints = {400, 408, 500, 503})
	@DisplayName("아마존 ses를 통해 리마인드 될 정보들을 전송한다.")
	void sendTrial_Success_WithNoException(int errorCode) throws InterruptedException {
		// given
		Receiver receiver = new Receiver(1L, "KAKAO", "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);

		given(findRemindableTargetsPort.findAllRemindablePlansByType(anyString(), anyString(), any()))
			.willReturn(List.of(remind));
		given(sesSendPlanRemindService.send(anyString(), anyString(), anyString(), anyString())).willReturn(errorCode);

		// when
		sendEmailAdapter.send("MORNING", TimeValue.now());
		sleep(100);

		// then
		then(sesSendPlanRemindService).should(times(5))
			.send(anyString(), anyString(), anyString(), anyString());
	}
}
