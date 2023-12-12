package com.newbarams.ajaja.module.plan.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class GetPlanAchieveServiceTest extends MockTestSupport {
	@InjectMocks
	private GetPlanAchieveService getPlanAchieveService;

	@Mock
	private PlanRepository planRepository;

	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	@DisplayName("조회하고 싶은 계획의 피드백을 통틀어서 달성률의 평균을 매긴다.")
	void getTotalAchieve_Success_WithNoException() {
		// given
		List<Feedback> feedbackList = monkey.giveMe(Feedback.class, 2);

		int calculatedAchieve =
			(feedbackList.get(0).getAchieve().getRate() + feedbackList.get(1).getAchieve().getRate()) / 2;

		Plan plan = monkey.giveMeOne(Plan.class);
		plan.updateAchieve(calculatedAchieve);

		// when
		given(planRepository.findById(any())).willReturn(Optional.of(plan));

		// then
		int totalAchieve = getPlanAchieveService.calculatePlanAchieve(1L);

		assertThat(totalAchieve).isEqualTo(calculatedAchieve);
	}

	@Test
	@DisplayName("조회하고 싶은 계획의 정보가 존재하지 않는 경우 예외를 던진다.")
	void getEmptyAchieve_Success_WithNoException() {
		// when
		given(planRepository.findById(any())).willReturn(Optional.empty());

		// then
		assertThatThrownBy(() ->
			getPlanAchieveService.calculatePlanAchieve(1L));
	}
}
