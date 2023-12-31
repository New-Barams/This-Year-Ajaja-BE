package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.MessageMapper;

class UpdateRemindInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private UpdateRemindInfoService updateRemindInfoService;

	@Mock
	private PlanRepository repository;
	@Mock
	private PlanQueryRepository planQueryRepository;
	@Mock
	private MessageMapper mapper;
	@Mock
	private Plan mockPlan;

	private Plan plan;
	private PlanRequest.UpdateRemind dto;
	private List<Message> messages;
	private RemindInfo info;

	@BeforeEach
	void setUp() {
		plan = sut.giveMeOne(Plan.class);
		dto = sut.giveMeOne(PlanRequest.UpdateRemind.class);
	}

	@Test
	@DisplayName("플랜의 리마인드 정보와 메세지를 수정한다.")
	void updateRemindInfo_Success_WithNNoException() {
		// given
		Long userId = 1L;
		Long planId = 1L;
		given(planQueryRepository.findByUserIdAndPlanId(anyLong(), anyLong())).willReturn(mockPlan);
		given(mapper.toDomain(dto.getMessages())).willReturn(messages);
		given(mapper.toDomain(dto)).willReturn(info);
		doNothing().when(mockPlan).updateRemind(any(), any());

		// when
		updateRemindInfoService.updateRemindInfo(userId, planId, dto);

		// then
		then(mockPlan).should(times(1)).updateRemind(any(), any());
	}

	@Test
	@DisplayName("조회된 정보가 없을 시에 예외를 던진다.")
	void updateRemindInfo_Fail_ByNotFoundPlan() {
		// given
		Long userId = 2L;
		Long planId = 2L;
		doThrow(AjajaException.class).when(planQueryRepository).findByUserIdAndPlanId(anyLong(), anyLong());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> updateRemindInfoService.updateRemindInfo(userId, planId, dto)
		);
	}

	@Test
	@DisplayName("변경 기간이 아니면 예외를 던진다.")
	void updateRemindInfo_Fail_ByInvalidUpdatableDate() {
		// given
		Long userId = 1L;
		Long planId = 1L;
		given(planQueryRepository.findByUserIdAndPlanId(anyLong(), anyLong())).willReturn(mockPlan);
		doThrow(AjajaException.class).when(mockPlan).updateRemind(any(), any());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> updateRemindInfoService.updateRemindInfo(userId, planId, dto)
		);
	}
}
