package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;

class FindPlanAdapterTest extends MockTestSupport {
	@InjectMocks
	private FindPlanAdapter findPlanAdapter;

	@Mock
	private LoadPlanService loadPlanService;

	private Plan plan;

	@BeforeEach
	void setUp() {
		plan = sut.giveMeOne(Plan.class);
	}

	@Test
	void findByUserIdAndPlanId_Success_WithNoException() {
		// given
		given(loadPlanService.loadByUserIdAndPlanId(anyLong(), anyLong())).willReturn(plan);

		// when
		Plan found = findPlanAdapter.findByUserIdAndPlanId(1L, 1L);

		// then
		assertThat(found).isEqualTo(plan);
	}
}
