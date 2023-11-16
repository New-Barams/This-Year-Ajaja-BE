package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

@ExtendWith(MockitoExtension.class)
class GetPlanServiceTest extends MockTestSupport {
	@InjectMocks
	private GetPlanInfoService getPlanInfoService;

	@Mock
	private PlanQueryRepository planQueryRepository;

	@Test
	@DisplayName("조회된 계획의 평균을 내서 총 달성률을 구한다.")
	void getPlanAchieveAverage_Success_WithNoException() {
		// given
		List<PlanInfoResponse.GetPlan> planInfos = monkey.giveMe(PlanInfoResponse.GetPlan.class, 2);
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(planInfos);

		// when
		getPlanInfoService.loadPlanInfo(1L);

		// then
		then(planQueryRepository).should(times(1)).findAllPlanByUserId(any());
	}

	@Test
	@DisplayName("조회된 계획들이 없는 경우 총 달성량은 0이 나온다.")
	void getPlanZeroAverage_Success_WithNoException() {
		// given
		List<PlanInfoResponse.GetPlan> planInfos = Collections.emptyList();
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(planInfos);

		// when
		getPlanInfoService.loadPlanInfo(1L);

		// then
		then(planQueryRepository).should(times(1)).findAllPlanByUserId(any());
	}
}
