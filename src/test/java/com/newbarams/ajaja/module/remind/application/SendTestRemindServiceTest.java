package com.newbarams.ajaja.module.remind.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindAddressPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendAlimtalkRemindPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendEmailRemindPort;

@RedisBasedTest
class SendTestRemindServiceTest extends MockTestSupport {
	@Autowired
	private SendTestRemindService sendTestRemindService;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@MockBean
	private FindRemindAddressPort findRemindAddressPort;
	@MockBean
	private SendEmailRemindPort sendEmailRemindPort;
	@MockBean
	private SendAlimtalkRemindPort sendAlimtalkRemindPort;

	private RemindAddress address;

	@BeforeEach
	void setUp() {
		address = sut.giveMeBuilder(RemindAddress.class)
			.set("type", "EMAIL")
			.sample();
	}

	@Test
	@DisplayName("유저에게 테스트 리마인드를 전송한다.")
	void send_Success_WithNoException() {
		// given
		given(findRemindAddressPort.findAddressByUserId(anyLong())).willReturn(address);
		doNothing().when(sendEmailRemindPort).send(any(), anyString(), anyString(), anyString());

		// when
		sendTestRemindService.send(1L);

		// then
		assertThat(redisTemplate.opsForValue().get(address.userEmail())).isEqualTo(1);
	}

	@Test
	@DisplayName("리마인드 전송을 실패할 경우 예외를 던진다.")
	void send_Fail_ByTaskFailed() {
		// given
		AjajaException exception = new AjajaException(ErrorCode.REMIND_TASK_FAILED);

		given(findRemindAddressPort.findAddressByUserId(anyLong())).willReturn(address);
		doThrow(exception).when(sendEmailRemindPort).send(any(), anyString(), anyString(), anyString());

		// when, then
		assertThatException().isThrownBy(() -> sendTestRemindService.send(1L));
	}

	@Test
	@DisplayName("당일 리마인드 전송 횟수가 3회 초과일 경우 예외를 던진다.")
	void send_Fail_ByRequestOverMax() {
		// given
		redisTemplate.opsForValue().set(address.userEmail(), 3);

		given(findRemindAddressPort.findAddressByUserId(anyLong())).willReturn(address);
		doNothing().when(sendEmailRemindPort).send(any(), anyString(), anyString(), anyString());

		// when, then
		assertThatException().isThrownBy(() -> sendTestRemindService.send(1L));
	}
}
