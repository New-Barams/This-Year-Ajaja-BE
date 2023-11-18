package com.newbarams.ajaja.module.remind.mapper;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindInfo;

class RemindMapperTest extends MockTestSupport {
	@InjectMocks
	private RemindMapper remindMapper;

	@Test
	@DisplayName("계획에 담긴 메세지의 개수만큼 리마인드 정보들을 반환한다.")
	void getFutureRemindList_Success_WithNoException() {
		// given
		Plan plan = monkey.giveMeOne(Plan.class);

		// when
		List<GetRemindInfo.FutureRemindResponse> futureRemindResponse
			= remindMapper.toFutureRemind(plan);

		// then
		Assertions.assertThat(futureRemindResponse.size()).isEqualTo(plan.getMessages().size());
	}
}
