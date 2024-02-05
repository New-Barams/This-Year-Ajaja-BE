package me.ajaja.module.remind.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.remind.mapper.RemindInfoMapper;

class FindTargetRemindAdapterTest extends MockTestSupport {
	@Spy
	@InjectMocks
	private FindTargetRemindAdapter findTargetRemindAdapter;
	@Mock
	private RemindInfoMapper mapper;

	@Test
	@DisplayName("계획id로 조회하면 해당 계획에 맞는 계획 값을 받는다.")
	void getRemindInfo_Success_WithNoException() {
		// // given
		// List<Message> messages = sut.giveMe(Message.class, 5);
		// RemindResponse.Message message = sut.giveMeOne(RemindResponse.Message.class);
		//
		// Plan plan = sut.giveMeBuilder(Plan.class)
		// 	.set("deleted", false)
		// 	.set("messages", messages)
		// 	.sample();
		//
		// // when
		// given(findPlanRemindAdapter.loadByUserIdAndPlanId(anyLong(), anyLong())).willReturn(plan);
		// given(mapper.toMessage(any())).willReturn(message);
		//
		// // then
		// assertThatNoException().isThrownBy(() ->
		// 	findPlanRemindAdapter.findByUserIdAndPlanId(1L, plan.getId())
		// );
	}

	@Test
	@DisplayName("조회된 계획이 없으면 예외를 던진다.")
	void getRemindInfo_Fail_WithNoException() {
		// given
		Plan plan = null;

		// when
		doThrow(AjajaException.class).when(findTargetRemindAdapter).loadByUserIdAndPlanId(anyLong(), anyLong());

		// then
		assertThatException().isThrownBy(
			() -> findTargetRemindAdapter.findByUserIdAndPlanId(1L, 1L)
		);

	}
}
