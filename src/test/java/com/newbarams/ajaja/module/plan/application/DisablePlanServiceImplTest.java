package com.newbarams.ajaja.module.plan.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;

class DisablePlanServiceImplTest extends MockTestSupport {
	@InjectMocks
	private DisablePlanServiceImpl disablePlanService;

	@Mock
	private PlanQueryRepository planQueryRepository;

	@Test
	void disable_Success() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		List<Plan> plans = sut.giveMe(Plan.class, 4);
		given(planQueryRepository.findAllCurrentPlansByUserId(any())).willReturn(plans);

		// when
		disablePlanService.disable(userId);

		// then
		then(planQueryRepository).should(times(1)).findAllCurrentPlansByUserId(any());
		plans.stream().map(Plan::getStatus)
			.forEach(status -> {
				assertThat(status.isCanRemind()).isFalse();
				assertThat(status.isCanAjaja()).isFalse();
				assertThat(status.isDeleted()).isTrue();
			});
	}
}
