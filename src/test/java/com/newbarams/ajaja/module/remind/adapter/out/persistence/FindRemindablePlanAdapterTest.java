package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;

class FindRemindablePlanAdapterTest extends MockTestSupport {
	@InjectMocks
	private FindRemindablePlanAdapter findRemindablePlanAdapter;

	@Mock
	private PlanQueryRepository planQueryRepository;

	private List<RemindMessageInfo> remindMessageInfos;

	@BeforeEach
	void setUp() {
		remindMessageInfos = sut.giveMe(RemindMessageInfo.class, 10);
	}

	@Test
	void findAllRemindablePlan() {
		// given
		given(planQueryRepository.findAllRemindablePlan(anyString(), any())).willReturn(remindMessageInfos);

		// when
		List<RemindMessageInfo> plans = findRemindablePlanAdapter.findAllRemindablePlan("MORING", new TimeValue());

		// then
		Assertions.assertThat(plans).isEqualTo(remindMessageInfos);
	}
}
