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

import me.ajaja.common.support.MonkeySupport;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanParam;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.domain.User;
import me.ajaja.module.user.mapper.UserMapper;

@SpringBootTest
@Transactional
class UpdatePlanServiceTest extends MonkeySupport {
	@Autowired
	private UpdatePlanService updatePlanService;
	@Autowired
	private SavePlanPort savePlanPort;
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

		plan = savePlanPort.save(Plan.create(
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
			.hasMessage(UNMODIFIABLE_DURATION.getMessage());
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
			.hasMessage(NOT_AUTHOR.getMessage());
	}
}
