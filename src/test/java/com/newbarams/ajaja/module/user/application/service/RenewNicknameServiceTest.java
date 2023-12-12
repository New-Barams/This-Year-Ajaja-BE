package com.newbarams.ajaja.module.user.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.Nickname;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

class RenewNicknameServiceTest extends MockTestSupport {
	@InjectMocks
	private RenewNicknameService renewNicknameService;

	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("닉네임 갱신을 요청하면 새로운 이름으로 변경되어야 한다.")
	void renew_Success_WithNewNickname() {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("email", new Email("Ajaja@me.com"))
			.sample();

		Nickname oldNickname = user.getNickname();
		given(retrieveUserService.loadExistUserById(any())).willReturn(user);
		given(userRepository.save(any())).willReturn(user);

		// when
		String newNickname = renewNicknameService.renew(user.getId());

		// then
		assertThat(oldNickname.getNickname()).isNotEqualTo(newNickname);
		then(userRepository).should(times(1)).save(any());
	}
}
