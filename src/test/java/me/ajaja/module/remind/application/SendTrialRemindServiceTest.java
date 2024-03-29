// package me.ajaja.module.remind.application;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.BDDMockito.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.redis.core.RedisTemplate;
//
// import me.ajaja.common.annotation.RedisBasedTest;
// import me.ajaja.common.support.MockTestSupport;
// import me.ajaja.module.remind.application.port.out.FindRemindAddressPort;
// import me.ajaja.module.remind.domain.Receiver;
// import me.ajaja.module.remind.domain.Remind;
// import me.ajaja.module.remind.domain.Target;
//
// @RedisBasedTest
// class SendTrialRemindServiceTest extends MockTestSupport {
// 	@Autowired
// 	private SendTrialRemindService sendTestRemindService;
// 	@Autowired
// 	private RedisTemplate<String, Object> redisTemplate;
//
// 	@MockBean
// 	private FindRemindAddressPort findRemindAddressPort;
// 	@MockBean
// 	private RemindStrategyFactory factory;
// 	@Mock
// 	private SendAlimtalkRemindAdapter sendAlimtalkRemindAdapter;
//
// 	private Remind remind;
//
// 	@BeforeEach
// 	void setUp() {
// 		Receiver receiver = new Receiver(1L, "KAKAO", "email@ajaja.com", "01011112222");
// 		Target target = sut.giveMeOne(Target.class);
// 		String message = "화이팅";
// 		remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);
//
// 		redisTemplate.delete("01011112222");
// 	}
//
// 	@Test
// 	@DisplayName("유저에게 테스트 리마인드를 전송한다.")
// 	void send_Success_WithNoException() {
// 		// given
// 		given(findRemindAddressPort.findRemindByUserId(anyLong())).willReturn(remind);
// 		given(factory.get(anyString())).willReturn(sendAlimtalkRemindAdapter);
//
// 		// when
// 		sendTestRemindService.send(1L);
//
// 		// then
// 		assertThat(redisTemplate.opsForValue().get(remind.getPhoneNumber())).isEqualTo(1);
// 	}
//
// 	@Test
// 	@DisplayName("당일 리마인드 전송 횟수가 3회 초과일 경우 예외를 던진다.")
// 	void send_Fail_ByRequestOverMax() {
// 		// given
// 		redisTemplate.opsForValue().set(remind.getPhoneNumber(), 3);
//
// 		given(findRemindAddressPort.findRemindByUserId(anyLong())).willReturn(remind);
// 		given(factory.get(anyString())).willReturn(sendAlimtalkRemindAdapter);
//
// 		// when, then
// 		assertThatException().isThrownBy(() -> sendTestRemindService.send(1L));
// 	}
// }
