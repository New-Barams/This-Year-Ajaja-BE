package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
	void updateRemindInfo_Fail_ByNotFoundPlan() {
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
