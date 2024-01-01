package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;

class CreateRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateRemindService createRemindService;

	@Mock
	private RemindRepository remindRepository;

	@Test
	@DisplayName("보낸 리마인드 정보를 담은 리마인드 객체를 저장한다.")
	void createRemind_Success_WithNoException() {
		// given
		List<Message> messages = sut.giveMe(Message.class, 13);
		RemindInfo info = sut.giveMeBuilder(RemindInfo.class).set("remindTerm", 6).sample();
		Plan plan = sut.giveMeBuilder(Plan.class)
			.set("messages", messages)
			.set("info", info)
			.sample();

		// when
		// createRemindService.createRemind(plan, new TimeValue());

		// then
		// then(remindRepository).should(times(1)).save(any());
	}
}
