package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.model.RemindMessageInfo;
import com.newbarams.ajaja.module.remind.application.port.out.FindRemindablePlanPort;
import com.newbarams.ajaja.module.remind.application.port.out.SendPlanInfoRemindPort;
import com.newbarams.ajaja.module.remind.domain.PlanInfo;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.UserInfo;

class SchedulingRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private SchedulingRemindService schedulingRemindService;

	@Mock
	private FindRemindablePlanPort findRemindablePlanPort;
	@Mock
	private SendPlanInfoRemindPort sendPlanInfoRemindPort;
	@Mock
	private CreateRemindService createRemindService;

	@Mock
	private Plan plan;

	private List<RemindMessageInfo> remindMessage;

	@Test
	@DisplayName("1월을 제외하고 리마인드 가능한 계획만큼 리마인드를 보낸다.")
	void scheduleMorningRemind_Success_withNoException() {
		// given
		UserInfo userInfo = new UserInfo(1L, "yamsang2002@naver.com");
		PlanInfo planInfo = new PlanInfo(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(userInfo, planInfo, message, Remind.Type.AJAJA, 3, 1);

		given(findRemindablePlanPort.findAllRemindablePlan(anyString(), any())).willReturn(List.of(remind, remind));

		// when,then
		schedulingRemindService.scheduleMorningRemind();
		then(sendPlanInfoRemindPort).should(times(2)).send(any(), any(), any(), anyLong());
	}
}
