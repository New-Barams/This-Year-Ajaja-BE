package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;

class LoadRemindInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadRemindInfoService loadRemindInfoService;
	@Mock
	private LoadPlanService loadPlanService;
	@Mock
	private RemindMapper remindMapper;

	@Test
	@DisplayName("계획id로 조회하면 해당 계획에 맞는 리마인드 응답을 받는다.")
	void getRemindInfo_Success_WithNoException() {
		// given
		Plan plan = monkey.giveMeOne(Plan.class);

		// when
		given(loadPlanService.loadPlanOrElseThrow(any())).willReturn(plan);
		given(remindMapper.toFutureRemind(any())).willReturn(Collections.emptyList());

		// then
		Assertions.assertThatNoException().isThrownBy(
			() -> loadRemindInfoService.loadRemindInfo(1L)
		);
	}

	@Test
	void getRemindInfo_Fail_WithNoException() {
		// given
		Plan plan = null;

		// when
		doThrow(AjajaException.class).when(loadPlanService).loadPlanOrElseThrow(any());

		// then
		Assertions.assertThatException().isThrownBy(
			() -> loadRemindInfoService.loadRemindInfo(1L)
		);

	}
}
