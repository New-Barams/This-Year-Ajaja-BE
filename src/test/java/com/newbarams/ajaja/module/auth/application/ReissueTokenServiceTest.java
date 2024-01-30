package com.newbarams.ajaja.module.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.security.jwt.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.JwtValidator;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

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
		AuthResponse.Token tokens = sut.giveMeOne(AuthResponse.Token.class);
		given(jwtGenerator.reissue(any(), anyString())).willReturn(tokens);

		// when
		AuthResponse.Token response = reissueTokenService.reissue(tokens.getAccessToken(), tokens.getRefreshToken());

		// then
		then(jwtGenerator).should(times(1)).reissue(any(), anyString());
		then(jwtValidator).should(times(1)).validateReissuableAndExtractId(any(), anyString());
		assertThat(response).usingRecursiveComparison().isEqualTo(tokens);
	}
}
