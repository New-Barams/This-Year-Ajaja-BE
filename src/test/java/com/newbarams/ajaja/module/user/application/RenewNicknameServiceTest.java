package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.Nickname;
import com.newbarams.ajaja.module.user.domain.User;

class RenewNicknameServiceTest extends MockTestSupport {
	@InjectMocks
	private RefreshNicknameService renewNicknameService;

	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private ApplyChangePort applyChangePort;

	@Test
	@DisplayName("닉네임 갱신을 요청하면 새로운 이름으로 변경되어야 한다.")
	void renew_Success_WithNewNickname() {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("email", Email.init("ajaja@me.com"))
			.sample();

		Nickname oldNickname = user.getNickname();

		given(retrieveUserService.loadExistById(any())).willReturn(user);
		willDoNothing().given(applyChangePort).apply(any());

		// when
		renewNicknameService.refresh(user.getId());

		// then
		assertThat(oldNickname).isNotEqualTo(user.getNickname());
		then(retrieveUserService).should(times(1)).loadExistById(any());
		then(applyChangePort).should(times(1)).apply(any());
	}
}
