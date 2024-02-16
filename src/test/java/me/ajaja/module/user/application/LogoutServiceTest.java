package me.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.security.jwt.JwtExpirer;
import me.ajaja.module.user.application.port.out.CheckExistUserPort;

class LogoutServiceTest extends MockTestSupport {
	@InjectMocks
	private LogoutService logoutService;

	@Mock
	private CheckExistUserPort checkExistUserPort;
	@Mock
	private JwtExpirer jwtExpirer;

	@Test
	@DisplayName("사용자가 로그아웃하면 토큰이 삭제되어야 한다.")
	void logout_Success_ThenTokenRemoved() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		given(checkExistUserPort.isExist(any())).willReturn(true);

		// when
		logoutService.logout(userId);

		// then
		then(checkExistUserPort).should(times(1)).isExist(any());
		then(jwtExpirer).should(times(1)).expire(any());
	}

	@Test
	@DisplayName("존재하지 않는 사용자를 로그아웃시키면 예외를 던진다.")
	void logout_Fail_ByNotExistUser() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		given(checkExistUserPort.isExist(any())).willReturn(false);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> logoutService.logout(userId));
		then(jwtExpirer).shouldHaveNoInteractions();
	}
}
