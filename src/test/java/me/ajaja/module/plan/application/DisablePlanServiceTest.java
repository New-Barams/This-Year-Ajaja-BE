package me.ajaja.module.plan.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.plan.application.port.out.FindAllPlansPort;
import me.ajaja.module.plan.domain.Plan;

class DisablePlanServiceTest extends MockTestSupport {
	@InjectMocks
	private DisablePlanService disablePlanService;

	@Mock
	private FindAllPlansPort findAllPlansPort;

	@Test
	void disable_Success() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		List<Plan> plans = sut.giveMe(Plan.class, 4);
		given(findAllPlansPort.findAllCurrentPlansByUserId(any())).willReturn(plans);

		// when
		disablePlanService.disable(userId);

		// then
		then(findAllPlansPort).should(times(1)).findAllCurrentPlansByUserId(any());
		plans.stream()
			.map(Plan::getStatus)
			.forEach(status -> {
				assertThat(status.isCanRemind()).isFalse();
				assertThat(status.isCanAjaja()).isFalse();
				assertThat(status.isDeleted()).isTrue();
			});
	}
}
