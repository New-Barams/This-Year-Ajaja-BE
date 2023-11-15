package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.common.RedisBasedTest;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

@RedisBasedTest
class SendVerificationEmailServiceTest extends MonkeySupport {
	@Autowired
	private SendVerificationEmailService sendVerificationEmailService;

	@MockBean
	private SendCertificationService sendCertificationService;

	@MockBean
	private RetrieveUserService retrieveUserService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	@DisplayName("이메일 인증 메일을 전송하면 인증 전용 객체가 cache에 잘 저장되어야 한다.")
	void sendVerification_Success_SavedOnRedis() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		final String prefix = "AJAJA ";

		String email = "gmlwh124@Naver.com";
		User user = monkey.giveMeBuilder(User.class)
			.set("email", new Email(email))
			.sample();

		given(retrieveUserService.loadExistUserById(any())).willReturn(user);

		// when
		sendVerificationEmailService.sendVerification(userId, email);

		// then
		then(retrieveUserService).should(times(1)).loadExistUserById(any());
		then(sendCertificationService).should(times(1)).send(any(), any());

		Object saved = redisTemplate.opsForValue().get(prefix + userId);
		assertThat(saved).isNotNull();
		assertThat(saved).isInstanceOf(EmailVerification.class);

		EmailVerification verification = (EmailVerification)saved;
		assertThat(verification.email()).isEqualTo(email);
		assertThat(verification.certification()).isNotNull();
	}
}
