package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;

@SpringBootTest
class DeletePlanServiceTest {
	@Autowired
	DeletePlanService deletePlanService;

	@Autowired
	PlanRepository planRepository;

	@Test
	@DisplayName("planId가 존재하고, 삭제가능한 기간일 경우 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		Plan plan = Plan.builder()
			.userId(1L)
			.content(new Content("title", "description"))
			.info(new RemindInfo(12, 3, 15, "MORNING"))
			.isPublic(true)
			.messages(List.of(new Message("content")))
			.tags(null)
			.build();

		Plan saved = planRepository.save(plan);

		assertThatNoException().isThrownBy(
			() -> deletePlanService.delete(saved.getId(), "Thu JAN 09 2023")
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Not_Found_Plan() {
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		assertThatThrownBy(() -> deletePlanService.delete(planId, "Thu JAN 09 2023"))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(NOT_FOUND_PLAN.getMessage());
	}

	@Test
	@DisplayName("planId가 존재하지만, 삭제가능한 기간이 아닌 경우 계획을 삭제할 수 업다.")
	void deletePlan_Fail_By_Date() {
		Plan plan = Plan.builder()
			.userId(1L)
			.content(new Content("title", "description"))
			.info(new RemindInfo(12, 3, 15, "MORNING"))
			.isPublic(true)
			.messages(List.of(new Message("content")))
			.tags(null)
			.build();

		Plan saved = planRepository.save(plan);

		assertThatThrownBy(() -> deletePlanService.delete(saved.getId(), "Thu DEC 09 2023"))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}
}
