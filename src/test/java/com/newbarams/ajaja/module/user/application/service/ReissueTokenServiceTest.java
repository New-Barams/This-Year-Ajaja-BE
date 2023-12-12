package com.newbarams.ajaja.module.user.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.user.dto.UserResponse;

class ReissueTokenServiceTest extends MockTestSupport {
	@InjectMocks
	private ReissueTokenService reissueTokenService;

	@Mock
	private JwtGenerator jwtGenerator;
	@Mock
	private JwtValidator jwtValidator;
	@Mock
	private JwtParser jwtParser;

	@Test
	void reissue_Success_WithExpectedCall() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		UserResponse.Token tokens = sut.giveMeOne(UserResponse.Token.class);
		given(jwtParser.parseId(anyString())).willReturn(userId);
		given(jwtGenerator.generate(any())).willReturn(tokens);

		// when
		UserResponse.Token response = reissueTokenService.reissue(tokens.getAccessToken(), tokens.getRefreshToken());

		// then
		then(jwtParser).should(times(1)).parseId(anyString());
		then(jwtGenerator).should(times(1)).generate(any());
		then(jwtValidator).should(times(1)).validateReissueable(any(), anyString());
		assertThat(response).usingRecursiveComparison().isEqualTo(tokens);
	}
}
