package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;

@SpringBootTest
class UpdatePlanServiceTest {
	@Autowired
	private UpdatePlanService updatePlanService;

	@Autowired
	private GetPlanService getPlanService;

	@Autowired
	private PlanRepository planRepository;

	@Test
	@DisplayName("planId가 존재하고, 수정가능한 기간일 경우 계획을 수정할 수 있다.")
	void updatePlan_Success() {
		Plan plan = Plan.builder()
			.date("Thu JAN 09 2023")
			.userId(1L)
			.content(new Content("title", "description"))
			.info(new RemindInfo(12, 3, 15, "MORNING"))
			.isPublic(true)
			.messages(List.of(new Message("content")))
			.build();

		PlanRequest.Update request = new PlanRequest.Update("title", "des", 12, 1,
			15, "MORNING", true, true, true, null, List.of("message"));

		Plan saved = planRepository.save(plan);

		assertThatNoException().isThrownBy(
			() -> updatePlanService.update(saved.getId(), 1L, request, "Thu JAN 09 2023")
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_Not_Found_Plan() {
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		PlanRequest.Update request = new PlanRequest.Update("title", "des", 12, 1,
			15, "MORNING", true, true, true, null, List.of("message"));

		assertThatThrownBy(() -> updatePlanService.update(planId, 1L, request, "Thu JAN 09 2023"))
			.isInstanceOf(AjajaException.class)
			.hasMessage(NOT_FOUND_PLAN.getMessage());
	}

	@Test
	@DisplayName("planId가 존재하지만, 수정가능한 기간이 아닌 경우 계획을 수정할 수 업다.")
	void updatePlan_Fail_By_Not_Updatable_Date() {
		Plan plan = Plan.builder()
			.date("Thu JAN 09 2023")
			.userId(1L)
			.content(new Content("title", "description"))
			.info(new RemindInfo(12, 3, 15, "MORNING"))
			.isPublic(true)
			.messages(List.of(new Message("content")))
			.build();

		PlanRequest.Update request = new PlanRequest.Update("title", "des", 12, 1,
			15, "MORNING", true, true, true, null, List.of("message"));

		Plan saved = planRepository.save(plan);

		assertThatThrownBy(() -> updatePlanService.update(saved.getId(), 1L, request, "Thu DEC 09 2023"))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}
}
