package me.ajaja.module.plan.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;

class ValidateAjajaContentServiceTest extends MockTestSupport {
	@InjectMocks
	private ValidateContentService validateContentService;

	@Test
	@DisplayName("문장에 비속어가 포함되었을 경우, 비속어 포함 여부가 true로 반환된다.")
	void validateContent_Success() {
		// given
		PlanRequest.CheckBanWord request = new PlanRequest.CheckBanWord("ㅁㅊ", "ㅈㄹ");

		// when
		BanWordValidationResult result = validateContentService.check(request);

		// then
		assertThat(result.getTitle().isExistBanWord()).isTrue();
		assertThat(result.getDescription().isExistBanWord()).isTrue();
	}

	@Test
	@DisplayName("문장에 비속어가 포함되지 않았을 경우, 비속어 포함 여부가 false로 반환된다.")
	void validateContent_Success_WithoutBanWord() {
		// given
		PlanRequest.CheckBanWord request = new PlanRequest.CheckBanWord("바른말", "고운말");

		// when
		BanWordValidationResult result = validateContentService.check(request);

		// then
		assertThat(result.getTitle().isExistBanWord()).isFalse();
		assertThat(result.getDescription().isExistBanWord()).isFalse();
	}
}
