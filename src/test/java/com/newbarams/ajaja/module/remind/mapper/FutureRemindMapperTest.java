package com.newbarams.ajaja.module.remind.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

class FutureRemindMapperTest extends MockTestSupport {
	@InjectMocks
	private FutureRemindMapper futureRemindMapper;

	@Test
	@DisplayName("계획에 담긴 메세지의 개수만큼 리마인드 정보들을 반환한다.")
	void getFutureRemindList_Success_WithNoException() {
		// given
		Plan plan = monkey.giveMeOne(Plan.class);

		// when
		RemindResponse.CommonResponse futureRemindResponse
			= futureRemindMapper.toFutureRemind(plan);

		// then
		Assertions.assertThat(futureRemindResponse.futureRemindResponses().size()).isEqualTo(plan.getMessages().size());
	}
}
