package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

@SpringBootTest
@Transactional
class UpdatePlanServiceTest extends MonkeySupport {
	@Autowired
	private UpdatePlanService updatePlanService;
	@Autowired
	private PlanRepository planRepository;
	@Autowired
	private UserJpaRepository userRepository;
	@Autowired
	private UserMapper userMapper;

	private User user;
	private Plan plan;

	@BeforeEach
	void setup() {
		UserEntity entity = userMapper.toEntity(User.init(1L, "+82 1012345678", "ajaja@me.com"));
		user = userMapper.toDomain(userRepository.save(entity));

		plan = planRepository.save(Plan.create(
			new PlanParam.Create(
				1,
				user.getId(),
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				new PlanStatus(true, true),
				1,
				List.of(new Message("content", 3, 15))
			)
		));
	}

	@Test
	@DisplayName("planId가 존재하고, 수정가능한 기간일 경우 계획을 수정할 수 있다.")
	void updatePlan_Success() {
		// given
		PlanRequest.Update request =
			new PlanRequest.Update(1, "title", "des", true, true, null);

		// when, then
		assertThatNoException().isThrownBy(
			() -> updatePlanService.update(plan.getId(), user.getId(), request, 1)
		);
	}

	@Test
	@DisplayName("planId가 존재하지 않을 경우 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_Not_Found_Plan() {
		// given
		Long planId = Arbitraries.longs().lessOrEqual(-1L).sample();

		PlanRequest.Update request = new PlanRequest.Update(1, "title", "des",
			true, true, null);

		// when, then
		assertThatThrownBy(() -> updatePlanService.update(planId, 1L, request, 1))
			.isInstanceOf(AjajaException.class);
	}

	@Test
	@DisplayName("planId가 존재하지만, 수정가능한 기간이 아닌 경우 계획을 수정할 수 업다.")
	void updatePlan_Fail_By_Not_Updatable_Date() {
		// given
		PlanRequest.Update request =
			new PlanRequest.Update(1, "title", "des", true, true, null);

		// when, then
		assertThatThrownBy(() -> updatePlanService.update(plan.getId(), 1L, request, 12))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}

	@Test
	@DisplayName("계획 작성자가 아닌 경우 계획을 수정할 수 없다.")
	void updatePlan_Fail_By_User() {
		// given
		Long strangerId = 100L;

		PlanRequest.Update request =
			new PlanRequest.Update(1, "title", "des", true, true, null);

		// when, then
		assertThatThrownBy(() -> updatePlanService.update(plan.getId(), strangerId, request, 1))
			.isInstanceOf(AjajaException.class)
			.hasMessage(INVALID_USER_ACCESS.getMessage());
	}
}
