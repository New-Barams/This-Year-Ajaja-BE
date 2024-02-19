package me.ajaja.module.footprint.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.common.support.MonkeySupport;
import me.ajaja.module.footprint.dto.FootprintParam;

class FootprintTest extends MonkeySupport {
	private final FootprintFactory footprintFactory = new FootprintFactory();

	@Test
	@DisplayName("조건에 맞는 입력 값에 따라 유형별 발자취 도메인 생성에 성공 한다.")
	void create_FreeFootprint_Success_WithNoException() {
		FootprintParam.Create param = sut.giveMeOne(FootprintParam.Create.class);

		assertThatNoException().isThrownBy(() -> {
			footprintFactory.create(param);
		});
	}

	@Test
	@DisplayName("발자취 항목에 대한 값이 null 일 때 예외가 발생 한다.")
	void create_Fail_ByNoneContnets() {
		FootprintParam.Create param = sut.giveMeBuilder(FootprintParam.Create.class)
			.set("content", "")
			.set("keepContent", "")
			.set("problemContent", "")
			.set("tryContent", "")
			.sample();

		assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> footprintFactory.create(param));
	}
}
