package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;

import org.ahocorasick.trie.Emit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BanWordFilterTest {
	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("문장에 비속어가 포함되었을 경우, 비속어 단어와 위치를 반환한다.")
	void badWordFilter_Success() {
		// given
		Emit expected = new Emit(0, 1, "ㅁㅊ");
		Emit expected2 = new Emit(3, 4, "ㅈㄹ");

		// when
		Collection<Emit> result = BanWordFilter.validate("ㅁㅊ ㅈㄹ하네");

		// then
		assertThat(result).hasSize(2);  // 비속어 개수
		assertThat(result.stream().toList().get(0)).isEqualByComparingTo(expected);
		assertThat(result.stream().toList().get(1)).isEqualByComparingTo(expected2);
	}

	@Test
	@DisplayName("문장에 비속어가 포함되지 않았을 경우, 빈 컬렉션이 반환된다.")
	void badWordFilter_Success_WithoutBanWord() {
		// given, when
		Collection<Emit> result = BanWordFilter.validate("바른말 고운말");

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("문장에 비속어가 겹치게 존재하는 경우, 길이가 긴 비속어가 반환된다.")
	void badWordFilter_Success_WithIgnoreOverlaps() {
		// given
		Emit ignored = new Emit(0, 0, "썅");
		Emit expected = new Emit(0, 1, "썅년");

		// when
		Collection<Emit> result = BanWordFilter.validate("썅년");

		// then
		assertThat(result).hasSize(1);  // 비속어 개수
		assertThat(result.stream().toList().get(0)).isEqualByComparingTo(expected);
	}
}
