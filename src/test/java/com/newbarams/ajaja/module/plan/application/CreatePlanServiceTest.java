package com.newbarams.ajaja.module.plan.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.tag.application.CreatePlanTagService;

class CreatePlanServiceTest extends MockTestSupport {
	PlanRequest.Create request = sut.giveMeBuilder(PlanRequest.Create.class)
		.set("title", Arbitraries.strings().ofMaxLength(20))
		.set("description", Arbitraries.strings().ofMaxLength(250))
		.set("remindTime", "MORNING")
		.sample();

	PlanParam.Create param = new PlanParam.Create(
		1,
		1L,
		new Content("title", "des"),
		new RemindInfo(12, 6, 1, "MORNING"),
		new PlanStatus(true, true),
		1,
		List.of(new Message("message1", 6, 1), new Message("message2", 12, 1))
	);

	Plan savedPlan = new Plan(
		1L,
		1L,
		1,
		new Content("title", "des"),
		new RemindInfo(12, 6, 1, "MORNING"),
		new PlanStatus(true, true),
		List.of(new Message("message1", 6, 1), new Message("message2", 12, 1)),
		Collections.emptyList(),
		new TimeValue(Instant.now())
	);

	@InjectMocks
	private CreatePlanService createPlanService;
	@Mock
	private PlanQueryRepository planQueryRepository;
	@Mock
	private PlanRepository planRepository;
	@Mock
	private PlanMapper planMapper;
	@Mock
	private CreatePlanTagService createPlanTagService;

	@Test
	@DisplayName("요청이 들어오면 계획이 생성되고, 생성된 계획의 Id를 반환한다.")
	void createPlan_Success() {
		// given
		given(planMapper.toParam(anyLong(), (PlanRequest.Create)any(), anyInt())).willReturn(param);
		given(planRepository.save(any())).willReturn(savedPlan);
		given(planQueryRepository.countByUserId(anyLong())).willReturn(1L);
		given(createPlanTagService.create(anyLong(), anyList())).willReturn(Collections.emptyList());

		// when
		Long planId = createPlanService.create(1L, request, 1);

		// then
		assertThat(planId).isEqualTo(savedPlan.getId());
	}
}
