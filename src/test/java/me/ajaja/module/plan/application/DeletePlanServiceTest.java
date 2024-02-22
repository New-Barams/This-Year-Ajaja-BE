package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanParam;

@SpringBootTest
@Transactional
class DeletePlanServiceTest {
	@Autowired
	private DeletePlanService deletePlanService;
	@Autowired
	private SavePlanPort savePlanPort;

	private final Long userId = 1L;
	private Plan plan;

	@BeforeEach
	void setup() {
		plan = savePlanPort.save(Plan.create(
			new PlanParam.Create(
				1,
				userId,
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				new PlanStatus(true, true),
				1,
				List.of(new Message("content", 3, 15))
			)
		));
	}

	@Test
	@DisplayName("planId가 존재하고, 삭제가능한 기간일 경우 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		// given

		// when, then
		assertThatNoException().isThrownBy(
			() -> deletePlanService.delete(plan.getId(), userId, 1)
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Not_Found_Plan() {
		// given
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		// when, then
		assertThatThrownBy(() -> deletePlanService.delete(planId, userId, 1))
			.isInstanceOf(AjajaException.class);
	}

	@Test
	@DisplayName("planId가 존재하지만, 삭제가능한 기간이 아닌 경우 계획을 삭제할 수 업다.")
	void deletePlan_Fail_By_Date() {
		// given

		// when, then
		assertThatThrownBy(() -> deletePlanService.delete(plan.getId(), userId, 12))
			.isInstanceOf(AjajaException.class)
			.hasMessage(UNMODIFIABLE_DURATION.getMessage());
	}

	@Test
	@DisplayName("계획 작성자가 아닌 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_User() {
		// given
		Long strangerId = 100L;

		// when, then
		assertThatThrownBy(() -> deletePlanService.delete(plan.getId(), strangerId, 1))
			.isInstanceOf(AjajaException.class)
			.hasMessage(NOT_AUTHOR.getMessage());
	}
}
