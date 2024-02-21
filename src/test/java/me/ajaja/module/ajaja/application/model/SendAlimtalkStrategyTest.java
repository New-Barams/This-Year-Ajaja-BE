package me.ajaja.module.ajaja.application.model;

import static java.lang.Thread.*;
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
import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
import me.ajaja.infra.feign.ncp.model.NaverResponse;
import me.ajaja.module.ajaja.domain.Ajaja;
import me.ajaja.module.ajaja.domain.AjajaQueryRepository;
import me.ajaja.module.ajaja.mapper.AjajaMapper;
import me.ajaja.module.remind.application.model.RemindableAjaja;
import me.ajaja.module.remind.application.port.out.SaveAjajaRemindPort;
import me.ajaja.module.remind.util.RemindExceptionHandler;

class SendAlimtalkStrategyTest extends MockTestSupport {
	@InjectMocks
	private SendAlimtalkStrategy sendAlimtalkStrategy;

	@Mock
	private AjajaQueryRepository ajajaQueryRepository;
	@Mock
	private NaverSendAlimtalkFeignClient feignClient;
	@Mock
	private NaverCloudProperties properties;
	@Mock
	private RemindExceptionHandler exceptionHandler;
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

		NaverResponse.AlimTalk response = sut.giveMeBuilder(NaverResponse.AlimTalk.class)
			.set("statusCode", "202")
			.sample();

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("KAKAO")).willReturn(ajajas);
		given(feignClient.send(any(), any())).willReturn(response);
		given(mapper.toDomain(any(RemindableAjaja.class))).willReturn(domain);

		// when , then
		sendAlimtalkStrategy.send(TimeValue.now());
		sleep(100); // 비동기 메소드 처리 시간 확보
		then(feignClient).should(times(10)).send(any(), any());
	}

	@ParameterizedTest
	@ValueSource(strings = {"400", "401", "403", "404", "500"})
	@DisplayName("핸들링 가능한 예외가 발생한다면 리마인드 예외 핸들러에서 처리한다.")
	void send_Fail_ByExternalApiFailed(String errorCode) throws InterruptedException {
		// given
		RemindableAjaja ajaja = sut.giveMeOne(RemindableAjaja.class);

		NaverResponse.AlimTalk response = sut.giveMeBuilder(NaverResponse.AlimTalk.class)
			.set("statusCode", errorCode)
			.sample();

		given(ajajaQueryRepository.findRemindableAjajasByEndPoint("KAKAO")).willReturn(List.of(ajaja));
		given(feignClient.send(any(), any())).willReturn(response);
		given(mapper.toDomain(any(RemindableAjaja.class))).willReturn(domain);
		doNothing().when(exceptionHandler).handleRemindException(anyString(), anyString(), anyString());

		// when , then
		sendAlimtalkStrategy.send(TimeValue.now());
		sleep(100);
		then(exceptionHandler).should(times(1)).handleRemindException(anyString(), anyString(), anyString());
		then(saveAjajaRemindPort).should(never()).save(anyLong(), anyString(), anyLong(), anyString(), any());
	}
}
