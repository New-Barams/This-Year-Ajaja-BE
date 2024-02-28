package me.ajaja.module.footprint.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.common.support.MonkeySupport;
import me.ajaja.module.footprint.dto.FootprintRequest;

class FootprintTest extends MonkeySupport {
	private final FootprintFactory footprintFactory = new FootprintFactory();

	@Test
	@DisplayName("조건에 맞는 입력 값에 따라 유형별 발자취 도메인 생성에 성공 한다.")
	void create_FreeFootprint_Success_WithNoException() {
		Long userId = 1L;
		FootprintRequest.Create param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("title", "title")
			.set("content", "content")
			.set("keepContent", "keepContent")
			.set("problemContent", "problemContent")
			.set("tryContent", "tryContent")
			.sample();
		Footprint footprint = footprintFactory.init(userId, param);

		assertAll(
			() -> assertThat(footprint.getId()).isNull(),
			() -> assertThat(footprint.getTarget()).isNotNull(),
			() -> assertThat(footprint.getWriter()).isNotNull(),
			() -> assertThat(footprint.getType()).isNotNull().isIn(Footprint.Type.FREE, Footprint.Type.KPT),
			() -> assertThat(footprint.getTitle()).isNotNull(),
			() -> assertThat(footprint.isVisible()).isNotNull(),
			() -> assertThat(footprint.isDeleted()).isFalse()
		);
	}

	@Test
	@DisplayName("발자취 항목에 대한 값이 null 일 때 예외가 발생 한다.")
	void create_Fail_ByNoneContnets() {
		Long userId = 1L;
		FootprintRequest.Create param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("title", "title")
			.set("content", "")
			.set("keepContent", "")
			.set("problemContent", "")
			.set("tryContent", "")
			.sample();

		assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
			() -> footprintFactory.init(userId, param));
	}
}
