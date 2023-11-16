package com.newbarams.ajaja.module.feedback.service;

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

import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ExtendWith(MockitoExtension.class)
class GetTotalAchieveServiceTest {
	@InjectMocks
	private GetTotalAchieveService getTotalAchieveService;

	@Mock
	private PlanQueryRepository planQueryRepository;

	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	@DisplayName("그 해 유저의 피드백을 통틀어서 달성률의 평균을 매긴다.")
	void getTotalAchieve_Success_WithNoException() {
		// given
		PlanInfoResponse.GetPlan planInfo1 = new PlanInfoResponse.GetPlan("title", true, 50);
		PlanInfoResponse.GetPlan planInfo2 = new PlanInfoResponse.GetPlan("title", true, 100);

		int calculatedAchieve =
			(planInfo1.getAchieveRate() + planInfo2.getAchieveRate()) / 2;

		// when
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(List.of(planInfo1, planInfo2));

		// then
		int totalAchieve = getTotalAchieveService.calculateTotalAchieve(1L);

		Assertions.assertThat(totalAchieve).isEqualTo(calculatedAchieve);
	}

	@Test
	@DisplayName("만약 평가된 피드백이 없을 경우 점수는 0이 나온다.")
	void getEmptyAchieve_Success_WithNoException() {
		// given
		List<PlanInfoResponse.GetPlan> planInfoList = Collections.emptyList();

		// when
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(planInfoList);

		// then
		int totalAchieve = getTotalAchieveService.calculateTotalAchieve(1L);

		Assertions.assertThat(totalAchieve).isZero();
	}
}
