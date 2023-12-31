package com.newbarams.ajaja.module.ajaja.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanParam;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class SwitchAjajaServiceTest {
	@Autowired
	private SwitchAjajaService switchAjajaService;
	@Autowired
	private LoadPlanService loadPlanService;
	@Autowired
	private PlanRepository planRepository;

	private final Long userId = 1L;
	private Plan plan;

	@BeforeEach
	void setup() {
		plan = planRepository.save(Plan.create(
			new PlanParam.Create(
				1,
				userId,
				new Content("title", "description"),
				new RemindInfo(12, 3, 15, "MORNING"),
				true,
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
		PlanResponse.Detail detail = loadPlanService.loadByIdAndOptionalUser(userId, plan.getId());

		// then
		assertThat(detail.getAjajas()).isEqualTo(1L);
	}

	@Test
	@DisplayName("계획에 아좌좌가 취소되면, 아좌좌 개수 1이 줄어든다.")
	void switchAjaja_Success() {
		// given

		// when
		switchAjajaService.switchOrAddIfNotExist(userId, plan.getId());
		PlanResponse.Detail detail = loadPlanService.loadByIdAndOptionalUser(userId, plan.getId());

		switchAjajaService.switchOrAddIfNotExist(userId, plan.getId());
		PlanResponse.Detail detail2 = loadPlanService.loadByIdAndOptionalUser(userId, plan.getId());

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
