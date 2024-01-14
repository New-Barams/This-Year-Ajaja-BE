package com.newbarams.ajaja.infra.ses;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.newbarams.ajaja.common.support.MockTestSupport;

class SesSendPlanRemindAdapterTest extends MockTestSupport {
	@InjectMocks
	private SesSendPlanRemindAdapter planRemindService;

	@Mock
	private AmazonSimpleEmailService amazonSimpleEmailService;

	@Test
	@DisplayName("아마존 ses를 통해 리마인드 될 정보들을 전송한다.")
	void send_Success_WithNoException() {
		// when
		planRemindService.send("yamsang2002@naver.com", "계확", "화이팅", 1L);

		// then
		then(amazonSimpleEmailService).should(times(1)).sendEmail(any());
	}
}
