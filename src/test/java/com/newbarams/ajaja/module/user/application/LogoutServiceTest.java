package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.security.jwt.util.JwtRemover;
import com.newbarams.ajaja.module.user.domain.UserRepository;

class LogoutServiceTest extends MockTestSupport {
	@InjectMocks
	private LogoutService logoutService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtRemover jwtRemover;

	@Test
	@DisplayName("사용자가 로그아웃하면 토큰이 삭제되어야 한다.")
	void logout_Success_ThenTokenRemoved() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		given(userRepository.existsById(any())).willReturn(true);

		// when
		logoutService.logout(userId);

		// then
		then(userRepository).should(times(1)).existsById(any());
		then(jwtRemover).should(times(1)).remove(any());
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 로그아웃시키면 예외를 던진다.")
	void logout_Fail_ByNotExistUser() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		given(userRepository.existsById(any())).willReturn(false);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> logoutService.logout(userId));
		then(jwtRemover).shouldHaveNoInteractions();
	}
}
