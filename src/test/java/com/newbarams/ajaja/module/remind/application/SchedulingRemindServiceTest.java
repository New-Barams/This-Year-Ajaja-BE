package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;

class SchedulingRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private SchedulingRemindService schedulingRemindService;

	@Mock
	private CreateRemindService createRemindService;
	@Mock
	private SendPlanRemindService sendPlanRemindService;
	@Mock
	private PlanQueryRepository planQueryRepository;
	@Mock
	private Plan plan;

	private List<RemindMessageInfo> remindMessage;

	@Test
	@DisplayName("1월을 제외하고 리마인드 가능한 계획만큼 리마인드를 보낸다.")
	void scheduleMorningRemind_Success_withNoException() {
		// given
		List<Message> messages = sut.giveMe(Message.class, 11);
		RemindInfo info = sut.giveMeBuilder(RemindInfo.class)
			.set("remindTerm", 1)
			.sample();
		plan = sut.giveMeBuilder(Plan.class)
			.set("messages", messages)
			.set("info", info)
			.sample();

		remindMessage = sut.giveMeBuilder(RemindMessageInfo.class)
			.set("plan", plan)
			.sampleList(5);

		given(planQueryRepository.findAllRemindablePlan(anyString(), any())).willReturn(remindMessage);

		// when,then
		if (new TimeValue().getMonth() == 1) {
			Assertions.assertThatException().isThrownBy(() -> {
				schedulingRemindService.scheduleMorningRemind();
			});
		} else {
			schedulingRemindService.scheduleMorningRemind();
			then(sendPlanRemindService).should(times(5)).send(any(), any(), any(), anyLong());
		}

	}
}
