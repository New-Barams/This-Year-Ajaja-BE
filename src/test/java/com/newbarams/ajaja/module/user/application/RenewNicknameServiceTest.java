package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.Nickname;
import com.newbarams.ajaja.module.user.domain.User;

class RenewNicknameServiceTest extends MockTestSupport {
	@InjectMocks
	private RenewNicknameService renewNicknameService;

	@Mock
	private RetrieveUserService retrieveUserService;

	@Test
	@DisplayName("닉네임 갱신을 요청하면 새로운 이름으로 변경되어야 한다.")
	void renew_Success_WithNewNickname() {
		// given
		String mail = "gmlwh124@Naver.com";
		User user = monkey.giveMeBuilder(User.class)
			.set("email", new Email(mail))
			.sample();

		Nickname oldNickname = user.getNickname();
		given(retrieveUserService.loadExistUserById(any())).willReturn(user);

		// when
		String newNickname = renewNicknameService.renew(user.getId());

		// then
		assertThat(oldNickname.getNickname()).isNotEqualTo(newNickname);
	}
}
