package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;

class CreatePlanResponseServiceTest extends MockTestSupport {
	@InjectMocks
	private CreatePlanResponseService createPlanResponseService;

	@Test
	@DisplayName("조회된 계획의 평균을 내서 총 달성률을 구한다.")
	void getPlanAchieveAverage_Success_WithNoException() {
		// given
		List<PlanInfoResponse.GetPlan> planInfos = sut.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2023)
			.sampleList(2);
		int totalAchieveRate = (planInfos.get(0).achieveRate() + planInfos.get(1).achieveRate()) / 2;

		// when
		PlanInfoResponse.GetPlanInfoResponse planInfo = createPlanResponseService.createPlanInfo(2023, planInfos);

		// then
		Assertions.assertThat(planInfo.totalAchieveRate()).isEqualTo(totalAchieveRate);
	}

	@Test
	@DisplayName("조회된 계획들이 없는 경우 총 달성량은 0이 나온다.")
	void getPlanZeroAverage_Success_WithNoException() {
		// given
		List<PlanInfoResponse.GetPlan> planInfos = sut.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2023)
			.sampleList(0);

		// when
		PlanInfoResponse.GetPlanInfoResponse planInfo = createPlanResponseService.createPlanInfo(2023, planInfos);

		// then
		Assertions.assertThat(planInfo.totalAchieveRate()).isZero();
	}
}
