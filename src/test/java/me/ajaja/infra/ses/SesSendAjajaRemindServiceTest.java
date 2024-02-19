package me.ajaja.infra.ses;

import static java.lang.Thread.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.remind.application.model.RemindableAjaja;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

class SesSendAjajaRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private SesSendAjajaRemindService sendSesStrategy;

	@Mock
	private AjajaQueryRepository ajajaQueryRepository;
	@Mock
	private RemindExceptionHandler exceptionHandler;
	@Mock
	private AmazonSimpleEmailService amazonSimpleEmailService;
	@Mock
	private SaveAjajaRemindPort saveAjajaRemindPort;

	@Test
	@DisplayName("응원 가능한 아좌좌 수만큼 리마인드를 전송한다.")
	void send_Success_WithNoException() throws InterruptedException {
		// given
		List<RemindableAjaja> ajajas = sut.giveMe(RemindableAjaja.class, 10);

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("EMAIL")).willReturn(ajajas);

		// when , then
		sendSesStrategy.send(TimeValue.now());
		sleep(100); // 비동기 메소드 처리 시간 확보
		then(amazonSimpleEmailService).should(times(10)).sendEmail(any());
	}

	@Test
	@DisplayName("예외가 발생한다면 리마인드 예외 핸들러에서 처리한다.")
	void send_Fail_ByExternalApiFailed() throws InterruptedException {
		// given
		RemindableAjaja ajaja = sut.giveMeBuilder(RemindableAjaja.class)
			.set("email", "notEmailForm")
			.sample();

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("EMAIL")).willReturn(List.of(ajaja));
		doNothing().when(exceptionHandler).handleRemindException(anyString(), anyString(), anyString());

		// when , then
		sendSesStrategy.send(TimeValue.now());
		sleep(100);
		then(exceptionHandler).should(times(1)).handleRemindException(anyString(), anyString(), anyString());
		then(saveAjajaRemindPort).should(never()).save(anyLong(), anyString(), anyLong(), anyString(), any());
	}
}
