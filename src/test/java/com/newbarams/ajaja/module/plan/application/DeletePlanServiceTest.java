package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;

@SpringBootTest
class DeletePlanServiceTest {
	@Autowired
	DeletePlanService deletePlanService;

	@Autowired
	PlanRepository planRepository;

	@Test
	@DisplayName("planId가 존재하고, 삭제가능한 기간일 경우 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		Plan plan = Plan.create(
			new PlanParam.Create(
				1,
				1L,
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				true,
				1,
				List.of(new Message("content", 3, 15))
			)
		);

		Plan saved = planRepository.save(plan);

		assertThatNoException().isThrownBy(
			() -> deletePlanService.delete(saved.getId(), 1L, 1)
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Not_Found_Plan() {
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		assertThatThrownBy(() -> deletePlanService.delete(planId, 1L, 1))
			.isInstanceOf(AjajaException.class);
	}

	@Test
	@DisplayName("planId가 존재하지만, 삭제가능한 기간이 아닌 경우 계획을 삭제할 수 업다.")
	void deletePlan_Fail_By_Date() {
		Plan plan = Plan.create(
			new PlanParam.Create(
				1,
				1L,
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				true,
				1,
				List.of(new Message("content", 3, 15))
			)
		);

		Plan saved = planRepository.save(plan);

		assertThatThrownBy(() -> deletePlanService.delete(saved.getId(), 1L, 12))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}
}
