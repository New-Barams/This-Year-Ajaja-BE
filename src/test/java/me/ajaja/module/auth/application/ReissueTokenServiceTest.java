package me.ajaja.module.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.security.jwt.JwtGenerator;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.module.auth.dto.AuthResponse;

class ReissueTokenServiceTest extends MockTestSupport {
	@InjectMocks
	private ReissueTokenService reissueTokenService;

	@Mock
	private JwtGenerator jwtGenerator;
	@Mock
	private JwtParser jwtParser;

	@Test
	void reissue_Success_WithExpectedCall() {
		// given
		AuthResponse.Token tokens = sut.giveMeOne(AuthResponse.Token.class);
		given(jwtGenerator.reissue(any(), anyString())).willReturn(tokens);

		// when
		AuthResponse.Token response = reissueTokenService.reissue(tokens.getAccessToken(), tokens.getRefreshToken());

		// then
		then(jwtGenerator).should(times(1)).reissue(any(), anyString());
		then(jwtParser).should(times(1)).parseIdIfReissueable(anyString(), anyString());
		assertThat(response).usingRecursiveComparison().isEqualTo(tokens);
	}
}
