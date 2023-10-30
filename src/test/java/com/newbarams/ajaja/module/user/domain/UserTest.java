package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
	@Test
	void createUser_Success_WithNoException() {
		// given
		String email = "gmlwh124@naver.com";

		// when, then
		assertThatNoException()
			.isThrownBy(() -> new User(email));
	}
}
