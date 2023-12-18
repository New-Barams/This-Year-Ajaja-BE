package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

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
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.mapper.MessageMapper;

class UpdateRemindInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private UpdateRemindInfoService updateRemindInfoService;
	@Mock
	private PlanRepository repository;
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
		Long planId = 1L;
		given(repository.findById(anyLong())).willReturn(Optional.of(plan));
		given(mapper.toDomain(dto.getMessages())).willReturn(messages);
		given(mapper.toDomain(dto)).willReturn(info);

		// when,then
		Assertions.assertThatNoException().isThrownBy(
			() -> updateRemindInfoService.updateRemindInfo(planId, dto)
		);
	}

	@Test
	@DisplayName("조회된 정보가 없을 시에 예외를 던진다.")
	void updateRemindInfo_Fail_ByNotFoundPlan() {
		// given
		Long planId = 2L;
		given(repository.findById(anyLong())).willReturn(Optional.empty());

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> updateRemindInfoService.updateRemindInfo(planId, dto)
		);
	}

	@Test
	@DisplayName("변경 기간이 아니면 예외를 던진다.")
	void updateRemindInfo_Fail_ByInvalidUpdatableDate() {
		// given
		Long planId = 1L;
		given(repository.findById(anyLong())).willReturn(Optional.of(mockPlan));
		given(mapper.toDomain(dto.getMessages())).willReturn(messages);
		given(mapper.toDomain(dto)).willReturn(info);
		doThrow(AjajaException.class).when(mockPlan).updateRemind(info, messages);

		// when,then
		Assertions.assertThatException().isThrownBy(
			() -> updateRemindInfoService.updateRemindInfo(planId, dto)
		);
	}
}
