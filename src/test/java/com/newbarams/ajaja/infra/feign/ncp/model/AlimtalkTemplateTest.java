package com.newbarams.ajaja.infra.feign.ncp.model;

import static com.newbarams.ajaja.infra.feign.ncp.model.AlimtalkTemplate.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlimtalkTemplateTest {
	@Test
	@DisplayName("리마인드 양식에서 리마인드 템플릿을 만들면 예외를 발생하지 않아야 한다.")
	void remindContent_Success_WithOutException() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> REMIND.content("name", "message", 1, 1));
	}

	@Test
	@DisplayName("리마인드 양식에서 아좌좌 템플릿을 만들면 예외를 던져야 한다.")
	void remindContent_Fail_ByAjajaRequested() {
		// given

		// when, then
		assertThatExceptionOfType(UnsupportedOperationException.class)
			.isThrownBy(() -> REMIND.content(1L, "name"));
	}

	@Test
	@DisplayName("아좌좌 양식에서 리마인드 템플릿을 만들면 예외를 던져야 한다.")
	void ajajaContent_Fail_ByRemindRequested() {
		// given

		// when, then
		assertThatExceptionOfType(UnsupportedOperationException.class)
			.isThrownBy(() -> AJAJA.content("name", "message", 1, 1));
	}

	@Test
	@DisplayName("아좌좌 양식에서 리마인드 템플릿을 만들면 예외를 발생하지 않아야 한다.")
	void ajajaContent_Success_WithOutException() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> AJAJA.content(1L, "name"));
	}
}
