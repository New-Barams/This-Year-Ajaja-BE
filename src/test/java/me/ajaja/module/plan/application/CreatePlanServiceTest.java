package me.ajaja.module.plan.application;

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

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.plan.application.port.out.CountPlanPort;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanParam;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.application.CreatePlanTagsService;

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
		new BaseTime(Instant.now())
	);

	@InjectMocks
	private CreatePlanService createPlanService;
	@Mock
	private SavePlanPort savePlanPort;
	@Mock
	private CountPlanPort countPlanPort;
	@Mock
	private PlanMapper planMapper;
	@Mock
	private CreatePlanTagsService createPlanTagService;

	@Test
	@DisplayName("요청이 들어오면 계획이 생성되고, 생성된 계획의 Id를 반환한다.")
	void createPlan_Success() {
		// given
		given(planMapper.toParam(anyLong(), (PlanRequest.Create)any(), anyInt())).willReturn(param);
		given(savePlanPort.save(any())).willReturn(savedPlan);
		given(countPlanPort.countByUserId(anyLong())).willReturn(1L);
		given(createPlanTagService.create(anyLong(), anyList())).willReturn(Collections.emptyList());

		// when
		Long planId = createPlanService.create(1L, request, 1);

		// then
		assertThat(planId).isEqualTo(savedPlan.getId());
	}
}
