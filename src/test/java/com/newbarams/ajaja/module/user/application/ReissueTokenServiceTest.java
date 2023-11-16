package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.user.dto.UserResponse;

class ReissueTokenServiceTest extends MockTestSupport {
	@InjectMocks
	private ReissueTokenService reissueTokenService;

	@Mock
	private JwtGenerator jwtGenerator;

	@Mock
	private JwtValidator jwtValidator;

	@Test
	void reissue_Success_WithExpectedCall() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = monkey.giveMeOne(UserResponse.Token.class);
		given(jwtGenerator.generate(any())).willReturn(tokens);

		// when
		UserResponse.Token response = reissueTokenService.reissue(userId, tokens.accessToken(), tokens.refreshToken());

		// then
		then(jwtGenerator).should(times(1)).generate(any());
		then(jwtValidator).should(times(1)).validate(any(), anyString(), anyString());
		assertThat(response).usingRecursiveComparison().isEqualTo(tokens);
	}
}
