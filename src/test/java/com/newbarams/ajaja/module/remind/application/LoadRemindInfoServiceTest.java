package com.newbarams.ajaja.module.remind.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

class LoadRemindInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadRemindInfoService loadRemindInfoService;
	@Mock
	private LoadPlanService loadPlanService;
	@Mock
	private RemindQueryRepository remindQueryRepository;

	@Test
	@DisplayName("계획id로 조회하면 해당 계획에 맞는 리마인드 응답을 받는다.")
	void getRemindInfo_Success_WithNoException() {
		// given
		List<Message> messages = sut.giveMe(Message.class, 5);

		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("deleted", false)
			.set("messages", messages)
			.sample();

		RemindResponse.RemindInfo response = sut.giveMeOne(RemindResponse.RemindInfo.class);

		// when
		given(loadPlanService.loadPlanOrElseThrow(any())).willReturn(plan);
		given(remindQueryRepository.findAllReminds(any())).willReturn(response);

		// then
		assertThatNoException().isThrownBy(() ->
			loadRemindInfoService.loadRemindInfoResponse(1L, plan.getId())
		);
	}

	@Test
	void getRemindInfo_Fail_WithNoException() {
		// given
		Plan plan = null;

		// when
		doThrow(AjajaException.class).when(loadPlanService).loadPlanOrElseThrow(any());

		// then
		assertThatException().isThrownBy(
			() -> loadRemindInfoService.loadRemindInfoResponse(1L, 1L)
		);

	}
}
