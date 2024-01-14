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
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanPort;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;

class LoadRemindInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private GetRemindInfoService getRemindInfoService;
	@Mock
	private FindPlanPort findPlanPort;
	@Mock
	private RemindInfoMapper mapper;

	@Test
	@DisplayName("계획id로 조회하면 해당 계획에 맞는 리마인드 응답을 받는다.")
	void getRemindInfo_Success_WithNoException() {
		// given
		List<Message> messages = sut.giveMe(Message.class, 5);
		RemindResponse.Message message = sut.giveMeOne(RemindResponse.Message.class);

		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("deleted", false)
			.set("messages", messages)
			.sample();

		// when
		given(findPlanPort.findByUserIdAndPlanId(anyLong(), anyLong())).willReturn(plan);
		given(mapper.toMessage(any())).willReturn(message);

		// then
		assertThatNoException().isThrownBy(() ->
			getRemindInfoService.load(1L, plan.getId())
		);
	}

	@Test
	@DisplayName("조회된 계획이 없으면 예외를 던진다.")
	void getRemindInfo_Fail_WithNoException() {
		// given
		Plan plan = null;

		// when
		doThrow(AjajaException.class).when(findPlanPort).findByUserIdAndPlanId(anyLong(), anyLong());

		// then
		assertThatException().isThrownBy(
			() -> getRemindInfoService.load(1L, 1L)
		);

	}
}
