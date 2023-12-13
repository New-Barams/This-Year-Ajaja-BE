package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.remind.domain.RemindRepository;

class CreateRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateRemindService createRemindService;

	@Mock
	private RemindRepository remindRepository;

	@Test
	@DisplayName("보낸 리마인드 정보를 담은 리마인드 객체를 저장한다.")
	void sendRemindPerMonth_Success_WithNoException() {
		// given
		String message = "화이팅";

		// when
		createRemindService.createRemind(1L, 1L, message, 3, 15);

		// then
		then(remindRepository).should(times(1)).save(any());
	}
}
