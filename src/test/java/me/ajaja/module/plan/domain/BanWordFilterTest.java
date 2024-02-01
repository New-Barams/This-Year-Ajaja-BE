package me.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.ahocorasick.trie.Emit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BanWordFilterTest {
	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("문장에 비속어가 포함되었을 경우, 비속어 단어와 위치를 반환한다.")
	void badWordFilter_Success() {
		// when
		List<String> result = BanWordFilter.validate("ㅁㅊ ㅈㄹ하네");

		// then
		assertThat(result).hasSize(2);  // 비속어 개수
		assertThat(result.get(0)).isEqualTo("ㅁㅊ");
		assertThat(result.get(1)).isEqualTo("ㅈㄹ");
	}

	@Test
	@DisplayName("문장에 비속어가 포함되지 않았을 경우, 빈 컬렉션이 반환된다.")
	void badWordFilter_Success_WithoutBanWord() {
		// given, when
		List<String> result = BanWordFilter.validate("바른말 고운말");

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
		List<String> result = BanWordFilter.validate("썅년");

		// then
		assertThat(result).hasSize(1);  // 비속어 개수
		assertThat(result.get(0)).isEqualTo("썅년");
	}
}
