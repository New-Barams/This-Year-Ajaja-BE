package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

@RedisBasedTest
class VerifyCertificationServiceTest extends MockTestSupport {
	private static final String DEFAULT_EMAIL = "ajaja@me.com";

	@Autowired
	private VerifyCertificationService verifyCertificationService;
	@Autowired
	private CacheUtil cacheUtil;

	@MockBean
	private RetrieveUserService retrieveUserService;
	@MockBean
	private ApplyChangePort applyChangePort;

	private User user;

	@BeforeEach
	void setup() {
		user = sut.giveMeBuilder(User.class)
			.set("email", new Email(DEFAULT_EMAIL))
			.set("deleted", false)
			.sample();
	}

	@Test
	@DisplayName("인증 완료 시 유저 정보가 인증 완료 상태로 변경되어야 한다.")
	void verify_Success_WithUpdatedVerificationStatus() {
		// given
		String certification = RandomCertificationGenerator.generate();
		cacheUtil.saveEmailVerification(user.getId(), DEFAULT_EMAIL, certification);

		given(retrieveUserService.loadExistById(anyLong())).willReturn(user);
		willDoNothing().given(applyChangePort).apply(any());

		// when
		verifyCertificationService.verify(user.getId(), certification);

		// then
		then(retrieveUserService).should(times(1)).loadExistById(anyLong());
		then(applyChangePort).should(times(1)).apply(any());
		assertThat(user.getRemindEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(user.isVerified()).isTrue();
	}

	@Test
	@DisplayName("인증 정보를 찾을 수 없으면 인증에 실패한다.")
	void verify_Fail_ByNoCertification() {
		// given

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> verifyCertificationService.verify(user.getId(), "certification"))
			.withMessage(CERTIFICATION_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("인증 번호가 일치하지 않으면 예외를 던져야 한다.")
	void verify_Fail_ByWrongCertification() {
		// given
		String certification = RandomCertificationGenerator.generate();
		cacheUtil.saveEmailVerification(user.getId(), DEFAULT_EMAIL, certification);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> verifyCertificationService.verify(user.getId(), "certification"))
			.withMessage(CERTIFICATION_NOT_MATCH.getMessage());
	}
}
