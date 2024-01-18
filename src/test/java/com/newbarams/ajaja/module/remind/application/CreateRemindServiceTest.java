package com.newbarams.ajaja.module.remind.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.remind.adapter.out.persistence.SaveRemindAdapter;
import com.newbarams.ajaja.module.remind.domain.PlanInfo;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.UserInfo;

class CreateRemindServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateRemindService createRemindService;

	@Mock
	private SaveRemindAdapter saveRemindAdapter;

	@Test
	@DisplayName("보낸 리마인드 정보를 담은 리마인드 객체를 저장한다.")
	void save_Success_WithNoException() {
		// given
		UserInfo userInfo = new UserInfo(1L, "yamsang2002@naver.com");
		PlanInfo planInfo = new PlanInfo(1L, "화이팅");
		String message = "화이팅";
		Remind remind = new Remind(userInfo, planInfo, message, Remind.Type.AJAJA, 3, 1);

		// when
		createRemindService.create(remind, new TimeValue());

		// then
		then(saveRemindAdapter).should(times(1)).save(any());
	}
}
