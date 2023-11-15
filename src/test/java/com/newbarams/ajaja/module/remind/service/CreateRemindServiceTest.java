package com.newbarams.ajaja.module.remind.service;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.RemindInfo;
import com.newbarams.ajaja.module.remind.application.CreateRemindService;
import com.newbarams.ajaja.module.remind.repository.RemindRepository;

class CreateRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateRemindService createRemindService;

	@Mock
	private RemindRepository remindRepository;

	@Test
	@DisplayName("리마인드 텀이 1달이고 기간이 6개월인 경우 총 2월부터 6월까지 리마인드를 만든다.")
	void sendRemindPerMonth_Success_WithNoException() {
		// given
		List<Message> messages = monkey.giveMe(Message.class, 5);
		RemindInfo info = new RemindInfo(6, 1, 1, "MORNING");

		// when
		createRemindService.createRemind(1L, 1L, messages, info);

		// then
		then(remindRepository).should(times(5)).save(any());

	}

	@Test
	@DisplayName("리마인드 텀이 1달이 아닌 경우 리마인드 텀은 곧 시작 개월이다.")
	void sendRemindPerThreeMonth_Success_WithNoException() {
		// given
		List<Message> messages = monkey.giveMe(Message.class, 5);
		RemindInfo info = new RemindInfo(6, 3, 1, "MORNING");

		// when
		createRemindService.createRemind(1L, 1L, messages, info);

		// then
		then(remindRepository).should(times(2)).save(any());
	}
}
