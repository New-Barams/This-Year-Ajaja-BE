package me.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.remind.adapter.out.persistence.SaveRemindAdapter;
import me.ajaja.module.remind.domain.Receiver;
import me.ajaja.module.remind.domain.Remind;
import me.ajaja.module.remind.domain.Target;

class CreateRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateRemindService createRemindService;

	@Mock
	private SaveRemindAdapter saveRemindAdapter;

	@Test
	@DisplayName("보낸 리마인드 정보를 담은 리마인드 객체를 저장한다.")
	void save_Success_WithNoException() {
		// given
		Receiver receiver = new Receiver(1L, null, "yamsang2002@naver.com", null);
		Target target = new Target(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(receiver, target, message, Remind.Type.AJAJA, 3, 1);

		// when
		createRemindService.create(remind, TimeValue.now());

		// then
		then(saveRemindAdapter).should(times(1)).save(any());
	}
}
