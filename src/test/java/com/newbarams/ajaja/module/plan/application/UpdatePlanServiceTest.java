package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

@SpringBootTest
@Transactional
class UpdatePlanServiceTest {
	@Autowired
	private UpdatePlanService updatePlanService;

	@Autowired
	private LoadPlanService getPlanService;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("planId가 존재하고, 수정가능한 기간일 경우 계획을 수정할 수 있다.")
	void updatePlan_Success() {
		User user = User.init("abcde@naver.com", 1L);
		User savedUser = userRepository.save(user);

		Plan plan = Plan.create(
			new PlanParam.Create(
				1,
				savedUser.getId(),
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				true,
				1,
				List.of(new Message("content", 3, 15))
			)
		);

		PlanRequest.Update request = new PlanRequest.Update("title", "des",
			true, true, true, null);

		Plan saved = planRepository.save(plan);

		assertThatNoException().isThrownBy(
			() -> updatePlanService.update(saved.getId(), savedUser.getId(), request, 1)
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_Not_Found_Plan() {
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		PlanRequest.Update request = new PlanRequest.Update("title", "des",
			true, true, true, null);

		assertThatThrownBy(() -> updatePlanService.update(planId, 1L, request, 1))
			.isInstanceOf(AjajaException.class);
	}

	@Test
	@DisplayName("planId가 존재하지만, 수정가능한 기간이 아닌 경우 계획을 수정할 수 업다.")
	void updatePlan_Fail_By_Not_Updatable_Date() {
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

		PlanRequest.Update request = new PlanRequest.Update("title", "des",
			true, true, true, null);

		Plan saved = planRepository.save(plan);

		assertThatThrownBy(() -> updatePlanService.update(saved.getId(), 1L, request, 12))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}
}
