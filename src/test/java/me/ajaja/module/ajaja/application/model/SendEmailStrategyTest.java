package me.ajaja.module.ajaja.application.model;

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
import me.ajaja.infra.ses.SesSendAjajaRemindService;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.model.RemindableAjaja;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

class SendEmailStrategyTest extends MockTestSupport {

	@InjectMocks
	private SendEmailStrategy sendEmailStrategy;

	@Mock
	private AjajaQueryRepository ajajaQueryRepository;
	@Mock
	private RemindExceptionHandler exceptionHandler;
	@Mock
	private SesSendAjajaRemindService sesSendAjajaRemindService;
	@Mock
	private SaveAjajaRemindPort saveAjajaRemindPort;
	@Mock
	private AjajaMapper mapper;

	private Ajaja domain = sut.giveMeOne(Ajaja.class);

	@Test
	@DisplayName("응원 가능한 아좌좌 수만큼 리마인드를 전송한다.")
	void send_Success_WithNoException() throws InterruptedException {
		// given
		List<RemindableAjaja> ajajas = sut.giveMe(RemindableAjaja.class, 10);

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("EMAIL")).willReturn(ajajas);
		given(mapper.toDomain(any(RemindableAjaja.class))).willReturn(domain);
		given(sesSendAjajaRemindService.send(anyString(), anyString(), anyLong(), anyLong())).willReturn(202);

		// when
		sendEmailStrategy.send(TimeValue.now());
		sleep(100); // 비동기 메소드 처리 시간 확보

		// then
		then(sesSendAjajaRemindService).should(times(10))
			.send(anyString(), anyString(), anyLong(), anyLong());
	}

	@ParameterizedTest
	@ValueSource(ints = {400, 408, 500, 503})
	@DisplayName("예외가 발생한다면 리마인드 예외 핸들러에서 처리한다.")
	void send_Fail_ByExternalApiFailed(int errorCode) throws InterruptedException {
		// given
		RemindableAjaja ajaja = sut.giveMeBuilder(RemindableAjaja.class)
			.sample();

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("EMAIL")).willReturn(List.of(ajaja));
		given(mapper.toDomain(any(RemindableAjaja.class))).willReturn(domain);
		doNothing().when(exceptionHandler).handleRemindException(anyString(), anyString(), anyString());
		given(sesSendAjajaRemindService.send(anyString(), anyString(), anyLong(), anyLong())).willReturn(errorCode);

		// when
		sendEmailStrategy.send(TimeValue.now());
		sleep(100);

		// then
		then(exceptionHandler).should(times(1)).handleRemindException(anyString(), anyString(), anyString());
	}
}
