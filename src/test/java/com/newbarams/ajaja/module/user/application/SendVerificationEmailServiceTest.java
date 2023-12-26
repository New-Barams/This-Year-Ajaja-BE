package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.module.user.application.model.Verification;
import com.newbarams.ajaja.module.user.application.port.out.SendCertificationPort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

@RedisBasedTest
class SendVerificationEmailServiceTest extends MonkeySupport {
	@Autowired
	private SendVerificationEmailService sendVerificationEmailService;
	@Autowired
	private CacheUtil cacheUtil;

	@MockBean
	private SendCertificationPort sendCertificationPort;
	@MockBean
	private RetrieveUserService retrieveUserService;

	@Test
	@DisplayName("이메일 인증 메일을 전송하면 인증 전용 객체가 cache에 잘 저장되어야 한다.")
	void sendVerification_Success_SavedOnRedis() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		String email = "Ajaja@me.com";

		User user = sut.giveMeBuilder(User.class)
			.set("email", new Email(email))
			.sample();

		given(retrieveUserService.loadExistById(any())).willReturn(user);

		// when
		sendVerificationEmailService.sendVerification(userId, email);

		// then
		then(retrieveUserService).should(times(1)).loadExistById(any());
		then(sendCertificationPort).should(times(1)).send(any(), any());

		Verification verification = cacheUtil.getEmailVerification(userId);
		assertThat(verification).isNotNull();
		assertThat(verification.getTarget()).isEqualTo(email);
		assertThat(verification.getCertification()).isNotNull();
	}
}
