// package me.ajaja.module.remind.adapter.out.infra;
//
// import static java.lang.Thread.*;
// import static org.mockito.BDDMockito.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
//
// import me.ajaja.common.support.MockTestSupport;
// import me.ajaja.global.common.TimeValue;
// import me.ajaja.infra.feign.ncp.client.NaverCloudProperties;
// import me.ajaja.infra.feign.ncp.client.NaverSendAlimtalkFeignClient;
// import me.ajaja.infra.feign.ncp.model.NaverResponse;
// import me.ajaja.module.remind.application.SendAlimtalkRemindAdapter;
// import me.ajaja.module.remind.application.port.out.FindRemindableTargetsPort;
// import me.ajaja.module.remind.domain.Receiver;
// import me.ajaja.module.remind.domain.Remind;
// import me.ajaja.module.remind.domain.Target;
// import me.ajaja.global.exception.RemindExceptionHandler;
//
// class SendAlimtalkRemindAdapterTest extends MockTestSupport {
// 	@InjectMocks
// 	private SendAlimtalkRemindAdapter sendAlimtalkRemindAdapter;
//
// 	@Mock
// 	private NaverSendAlimtalkFeignClient feignClient;
// 	@Mock
// 	private NaverCloudProperties properties;
// 	@Mock
// 	private RemindExceptionHandler exceptionHandler;
// 	@Mock
// 	private FindRemindableTargetsPort findRemindableTargetsPort;
// 	private Remind remind;
//
// 	@BeforeEach
// 	void setUp() {
// 		Receiver receiver = new Receiver(1L, "KAKAO", "yamsang2002@naver.com", null);
// 		Target target = new Target(1L, "화이팅");
// 		String message = "화이팅";
// 		remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);
// 	}
//
// 	@Test
// 	@DisplayName("응원 가능한 아좌좌 수만큼 리마인드를 전송한다.")
// 	void send_Success_WithNoException() throws InterruptedException {
// 		// given
// 		NaverResponse.AlimTalk response = sut.giveMeBuilder(NaverResponse.AlimTalk.class)
// 			.set("statusCode", "202")
// 			.sample();
//
// 		given(findRemindableTargetsPort.findAllRemindablePlansByType(anyString(), anyString(), any()))
// 			.willReturn(List.of(remind));
// 		given(feignClient.send(any(), any())).willReturn(response);
//
// 		// when , then
// 		sendAlimtalkRemindAdapter.send("MORNING", TimeValue.now());
// 		sleep(100); // 비동기 메소드 처리 시간 확보
// 		then(feignClient).should(times(1)).send(any(), any());
// 	}
//
// 	@ParameterizedTest
// 	@ValueSource(strings = {"400", "401", "403", "404", "500"})
// 	@DisplayName("핸들링 가능한 예외가 발생한다면 재시도를 5회 진행한다.")
// 	void send_Fail_ByExternalApiFailed(String errorCode) throws InterruptedException {
// 		// given
// 		NaverResponse.AlimTalk response = sut.giveMeBuilder(NaverResponse.AlimTalk.class)
// 			.set("statusCode", errorCode)
// 			.sample();
//
// 		given(findRemindableTargetsPort.findAllRemindablePlansByType(anyString(), anyString(), any()))
// 			.willReturn(List.of(remind));
// 		given(feignClient.send(any(), any())).willReturn(response);
//
// 		// when , then
// 		sendAlimtalkRemindAdapter.send("MORNING", TimeValue.now());
// 		sleep(100);
// 		then(feignClient).should(times(5)).send(any(), any());
// 	}
// }
