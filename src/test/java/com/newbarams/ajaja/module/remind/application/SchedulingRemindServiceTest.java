package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.feedback.application.CreateFeedbackService;
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
	private CreateFeedbackService createFeedbackService;
	@Mock
	private PlanQueryRepository planQueryRepository;

	private List<RemindMessageInfo> remindMessage;

	@BeforeEach
	void setUp() {
		// List<Message> messages = sut.giveMe(Message.class, 12);
		// RemindInfo info = sut.giveMeBuilder(RemindInfo.class)
		// 	.set("remindTerm", 12)
		// 	.sample();
		// Plan plan = sut.giveMeBuilder(Plan.class)
		// 	.set("messages", messages)
		// 	.set("info", info)
		// 	.sample();
		//
		// remindMessage = sut.giveMeBuilder(RemindMessageInfo.class)
		// 	.set("plan", plan)
		// 	.sampleList(5);
		//
		// doNothing().when(createRemindService).createRemind(any(), any());
		// doNothing().when(sendPlanRemindService).send(any(), any(), any(), anyLong());
		// doNothing().when(createFeedbackService).create(anyLong(), anyLong());
		// given(planQueryRepository.findAllRemindablePlan(anyString(), any())).willReturn(remindMessage);
	}

	@Test
	@DisplayName("리마인드 가능한 계획만큼 리마인드를 보낸다.")
	void scheduleMorningRemind_Success_withNoException() {
		// when
		// schedulingRemindService.scheduleMorningRemind();

		// then
		// then(sendPlanRemindService).should(times(5)).send(any(), any(), any(), anyLong());
	}
}
