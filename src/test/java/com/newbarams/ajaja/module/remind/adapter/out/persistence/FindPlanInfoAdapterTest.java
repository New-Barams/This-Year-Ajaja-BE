package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;

class FindPlanInfoAdapterTest extends MockTestSupport {
	@InjectMocks
	private FindPlanInfoAdapter findPlanInfoAdapter;

	@Mock
	private PlanQueryRepository planQueryRepository;

	private List<PlanResponse.PlanInfo> planInfos;

	@BeforeEach
	void setUp() {
		planInfos = sut.giveMe(PlanResponse.PlanInfo.class, 4);
	}

	@Test
	@DisplayName("계획 Id에 맞는 정보를 불러온다.")
	void findAllPlanByUserId_Success_WithNoException() {
		// given
		given(planQueryRepository.findAllPlanByUserId(anyLong())).willReturn(planInfos);

		// when
		List<PlanResponse.PlanInfo> responses = findPlanInfoAdapter.findAllPlanInfosByUserId(1L);

		// then
		assertThat(responses).isEqualTo(planInfos);
	}
}
