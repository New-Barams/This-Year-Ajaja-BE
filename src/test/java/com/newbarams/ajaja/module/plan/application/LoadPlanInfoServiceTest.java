package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;

class LoadPlanInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadPlanInfoService loadPlanInfoService;

	@Mock
	private PlanQueryRepository planQueryRepository;
	@Mock
	private LoadTotalAchieveService loadTotalAchieveService;
	@Mock
	private PlanMapper mapper;

	@Test
	@DisplayName("처음 계획을 작성한 년도부터 현재 년도까지의 계획들을 조회한다.")
	void getPlanInfo_Success_WithNoException() {
		// given
		PlanInfoResponse.GetPlan planInfo1 = sut.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2023).sample();

		PlanInfoResponse.GetPlan planInfo2 = sut.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2021).sample();

		PlanInfoResponse.GetPlanInfoResponse response = sut.giveMeOne(PlanInfoResponse.GetPlanInfoResponse.class);

		int execute = 2023 - 2021 + 1;

		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(List.of(planInfo1, planInfo2));
		given(mapper.toResponse(anyInt(), anyInt(), anyList())).willReturn(response);

		//when
		loadPlanInfoService.loadPlanInfo(1L);

		// then
		then(loadTotalAchieveService).should(times(execute)).loadTotalAchieve(anyLong(), anyInt());
	}

	@Test
	@DisplayName("만약 조회된 계획들이 없다면 기본값들을 반환한다.")
	void getNoPlanInfo_Success_WithNoException() {
		// given
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(Collections.emptyList());

		//when
		List<PlanInfoResponse.GetPlanInfoResponse> planInfoResponses = loadPlanInfoService.loadPlanInfo(1L);

		// then
		Assertions.assertThat(planInfoResponses).isEmpty();
	}
}
