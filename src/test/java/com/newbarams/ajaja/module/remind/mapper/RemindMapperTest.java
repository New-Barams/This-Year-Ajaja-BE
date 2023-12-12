package com.newbarams.ajaja.module.remind.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

class RemindMapperTest extends MockTestSupport {
	@InjectMocks
	private RemindMapper remindMapper;

	@Test
	@DisplayName("계획에 담긴 메세지의 개수만큼 리마인드 정보들을 반환한다.")
	void getFutureRemindList_Success_WithNoException() {
		// given
		Plan plan = sut.giveMeOne(Plan.class);

		// when
		RemindResponse.CommonResponse futureRemindResponse = remindMapper.toFutureRemind(plan);

		// then
		assertThat(futureRemindResponse.futureRemindResponses()).hasSameSizeAs(plan.getMessages());
	}
}
