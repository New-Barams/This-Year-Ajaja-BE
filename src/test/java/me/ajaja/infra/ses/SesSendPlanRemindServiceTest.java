package me.ajaja.infra.ses;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.remind.domain.Receiver;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.domain.Target;

class SesSendPlanRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private SesSendPlanRemindService planRemindService;

	@Mock
	private AmazonSimpleEmailService amazonSimpleEmailService;

	@Test
	@DisplayName("아마존 ses를 통해 리마인드 될 정보들을 전송한다.")
	void send_Success_WithNoException() {
		// given
		Receiver receiver = new Receiver(1L, "KAKAO", "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);

		// when
		planRemindService
			.send(remind, "https://www.ajaja.me/main");

		// then
		Assertions.assertThatNoException().isThrownBy(()
			-> amazonSimpleEmailService.sendEmail(any()));
	}
}
