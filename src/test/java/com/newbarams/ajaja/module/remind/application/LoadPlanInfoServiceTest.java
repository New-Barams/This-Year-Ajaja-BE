package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanInfoPort;

class LoadPlanInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private GetPlanInfoService loadPlanInfoService;

	@Mock
	private FindPlanInfoPort findPlanInfoPort;
	@Mock
	private PlanMapper mapper;

	@Test
	@DisplayName("처음 계획을 작성한 년도부터 현재 년도까지의 계획들을 조회한다.")
	void getPlanInfo_Success_WithNoException() {
		// given
		PlanResponse.PlanInfo planInfo1 = sut.giveMeBuilder(PlanResponse.PlanInfo.class)
			.set("year", 2023).sample();

		PlanResponse.PlanInfo planInfo2 = sut.giveMeBuilder(PlanResponse.PlanInfo.class)
			.set("year", 2021).sample();

		PlanResponse.MainInfo response = sut.giveMeOne(PlanResponse.MainInfo.class);

		int execute = 2023 - 2021 + 1;

		given(findPlanInfoPort.findAllPlanInfosByUserId(any())).willReturn(List.of(planInfo1, planInfo2));
		given(mapper.toResponse(anyInt(), anyInt(), anyList())).willReturn(response);

		//when
		loadPlanInfoService.load(1L);

		// then
		then(mapper).should(times(execute)).toResponse(anyInt(), anyInt(), anyList());
	}

	@Test
	@DisplayName("만약 조회된 계획들이 없다면 기본값들을 반환한다.")
	void getNoPlanInfo_Success_WithNoException() {
		// given
		given(findPlanInfoPort.findAllPlanInfosByUserId(any())).willReturn(Collections.emptyList());

		//when
		List<PlanResponse.MainInfo> planInfoResponses = loadPlanInfoService.load(1L);

		// then
		Assertions.assertThat(planInfoResponses).isEmpty();
	}
}
