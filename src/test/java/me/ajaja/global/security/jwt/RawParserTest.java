package me.ajaja.global.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import me.ajaja.module.auth.dto.AuthResponse;

@SpringBootTest
class RawParserTest {
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private RawParser rawParser;

	@MockBean
	private TokenCache cache;

	@Test
	@DisplayName("파싱 시도 시에 토큰이 유효하다면 true를 리턴해야 한다.")
	void tryParse_Success_WithValidToken() {
		// given
		willDoNothing().given(cache).save(anyString(), anyString(), anyByte());
		AuthResponse.Token token = jwtGenerator.login(1L);

		// when
		boolean shouldBeTrue = rawParser.tryParse(token.getAccessToken());

		// then
		assertThat(shouldBeTrue).isTrue();
	}

	@Test
	@DisplayName("파싱 시도 시에 토큰이 유효하지 않다면 false를 리턴해야 한다.")
	void tryParse_Fail_ByInvalidToken() {
		// given
		String invalidToken = """
			eyJhbGciOiJIUzI1NiJ9.
			eyJuYW1lIjoiSGVqb3cifQ.
			SI7XBRHE_95nkxQ69SiiCQcqDkZ-FW1RdxNL1DmAAAg
			""";

		// when
		boolean shouldBeFalse = rawParser.tryParse(invalidToken);

		// then
		assertThat(shouldBeFalse).isFalse();
	}
}
