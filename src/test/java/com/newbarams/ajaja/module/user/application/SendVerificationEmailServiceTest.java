package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.RedisBasedTest;

@RedisBasedTest
class SendVerificationEmailServiceTest {
	@Autowired
	private SendVerificationEmailService sendVerificationEmailService;

	@MockBean
	private SendCertificationService sendCertificationService;

	@MockBean
	private UpdateRemindEmailService updateRemindEmailService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	@DisplayName("이메일 인증 메일을 전송하면 cache에 잘 저장되어야 한다.")
	void sendVerification_Success_SavedOnRedis() {
		// given
		String prefix = "AJAJA ";
		String email = "gmlwh124@Naver.com";

		// when
		sendVerificationEmailService.sendVerification(1L, email);

		// then
		then(updateRemindEmailService).should(times(1)).updateIfDifferent(any(), any());
		then(sendCertificationService).should(times(1)).send(any(), any());

		Object saved = redisTemplate.opsForValue().get(prefix + email);
		assertThat(saved).isNotNull();
		assertThat(saved).isInstanceOf(String.class);
	}
}
