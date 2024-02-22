package me.ajaja.module.ajaja.application;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.LoadPlanDetailUseCase;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Content;
import me.ajaja.module.plan.domain.Message;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanStatus;
import me.ajaja.module.plan.domain.RemindInfo;
import me.ajaja.module.plan.dto.PlanParam;
import me.ajaja.module.plan.dto.PlanResponse;

@SpringBootTest
@Transactional
class SwitchAjajaServiceTest {
	@Autowired
	private SwitchAjajaService switchAjajaService;
	@Autowired
	private LoadPlanDetailUseCase loadPlanDetailUseCase;
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
	@DisplayName("계획에 아좌좌가 추가되면 계획의 아좌좌 개수 1이 추가된다.")
	void addAjaja_Success() {
		// given

		// when
		switchAjajaService.switchOrAddIfNotExist(userId, plan.getId());
		PlanResponse.Detail detail = loadPlanDetailUseCase.loadByIdAndOptionalUser(userId, plan.getId());

		// then
		assertThat(detail.getAjajas()).isEqualTo(1L);
	}

	@Test
	@DisplayName("계획에 아좌좌가 취소되면, 아좌좌 개수 1이 줄어든다.")
	void switchAjaja_Success() {
		// given

		// when
		switchAjajaService.switchOrAddIfNotExist(userId, plan.getId());
		PlanResponse.Detail detail = loadPlanDetailUseCase.loadByIdAndOptionalUser(userId, plan.getId());

		switchAjajaService.switchOrAddIfNotExist(userId, plan.getId());
		PlanResponse.Detail detail2 = loadPlanDetailUseCase.loadByIdAndOptionalUser(userId, plan.getId());

		// then
		assertThat(detail.getAjajas()).isEqualTo(1L);
		assertThat(detail2.getAjajas()).isEqualTo(0);
	}

	@Test
	@DisplayName("계획이 존재하지 않을 경우 아좌좌를 추가할 수 없다.")
	void addAjaja_Fail_By_Not_Found_Plan() {
		// given
		Long nonExistId = -1L;

		// when, then
		assertThatThrownBy(() -> switchAjajaService.switchOrAddIfNotExist(userId, nonExistId))
			.isInstanceOf(AjajaException.class)
			.hasMessage(AjajaException.withId(nonExistId, NOT_FOUND_PLAN).getMessage());
	}
}
