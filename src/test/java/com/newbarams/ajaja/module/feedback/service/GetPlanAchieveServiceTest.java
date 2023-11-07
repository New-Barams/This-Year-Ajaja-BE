package com.newbarams.ajaja.module.feedback.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ExtendWith(MockitoExtension.class)
class GetPlanAchieveServiceTest {
	@InjectMocks
	private GetPlanAchieveService getPlanAchieveService;

	@Mock
	private FeedbackRepository feedbackRepository;

	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	@DisplayName("조회하고 싶은 계획의 피드백을 통틀어서 달성률의 평균을 매긴다.")
	void getTotalAchieve_Success_withNoException() {
		// given
		Feedback feedback1 = sut.giveMeOne(Feedback.class);
		Feedback feedback2 = sut.giveMeOne(Feedback.class);

		List<Feedback> feedbackList = List.of(feedback1, feedback2);

		int calculatedAchieve = (feedback1.getAchieve().getRate() + feedback2.getAchieve().getRate()) / 2;

		// when
		given(feedbackRepository.findAllByPlanIdIdAndCreatedYear(any())).willReturn(feedbackList);

		// then
		int totalAchieve = getPlanAchieveService.loadPlanAchieve(1L);

		Assertions.assertThat(totalAchieve).isEqualTo(calculatedAchieve);
	}

	@Test
	@DisplayName("만약 계획의 평가된 피드백이 없을 경우 점수는 0이 나온다.")
	void getEmptyAchieve_Success_withNoException() {
		// given
		List<Feedback> feedbackList = Collections.emptyList();

		// when
		given(feedbackRepository.findAllByPlanIdIdAndCreatedYear(any())).willReturn(feedbackList);

		// then
		int totalAchieve = getPlanAchieveService.loadPlanAchieve(1L);

		Assertions.assertThat(totalAchieve).isZero();
	}
}
