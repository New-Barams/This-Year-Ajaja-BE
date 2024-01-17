package com.newbarams.ajaja.module.feedback.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.infra.model.AchieveInfo;

class LoadTotalAchieveServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadTotalAchieveService loadTotalAchieveService;

	@Mock
	private FeedbackQueryRepository feedbackQueryRepository;

	@Test
	@DisplayName("그 해 유저의 피드백을 통틀어서 달성률의 평균을 매긴다.")
	void getTotalAchieve_Success_WithNoException() {
		// given
		List<AchieveInfo> achieveInfo = sut.giveMeBuilder(AchieveInfo.class).set("achieve", 50).sampleList(2);

		given(feedbackQueryRepository.findAchievesByUserIdAndYear(anyLong(), anyInt())).willReturn(achieveInfo);
		int calculatedAchieve = 50;

		// when
		int totalAchieve = loadTotalAchieveService.loadTotalAchieveByUserId(1L, 2023);

		// then
		Assertions.assertThat(totalAchieve).isEqualTo(calculatedAchieve);
	}

	@Test
	@DisplayName("만약 평가된 피드백이 없을 경우 점수는 0이 나온다.")
	void getEmptyAchieve_Success_WithNoException() {
		// given
		List<AchieveInfo> achieveInfos = Collections.emptyList();

		// when
		given(feedbackQueryRepository.findAchievesByUserIdAndYear(anyLong(), anyInt())).willReturn(achieveInfos);

		// then
		int totalAchieve = loadTotalAchieveService.loadTotalAchieveByUserId(1L, 203);

		Assertions.assertThat(totalAchieve).isZero();
	}
}
