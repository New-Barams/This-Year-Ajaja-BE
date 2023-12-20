package com.newbarams.ajaja.module.user.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.application.port.out.DisablePlanPort;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

class WithdrawServiceTest extends MockTestSupport {
	@InjectMocks
	private WithdrawService withdrawService;

	@Mock
	private DisablePlanPort disablePlanPort;
	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private DisconnectOauthPort disconnectOauthPort;
	@Spy
	private UserRepository userRepository;

	@Test
	void withdraw_Success() {
		// given
		User user = sut.giveMeBuilder(User.class)
				.set("email", new Email("Ajaja@me.com"))
				.set("deleted", false)
				.sample();

		given(retrieveUserService.loadExistById(any())).willReturn(user);
		willDoNothing().given(disconnectOauthPort).disconnect(any());
		willDoNothing().given(disablePlanPort).disable(any());

		// when
		withdrawService.withdraw(user.getId());

		// then
		then(retrieveUserService).should(times(1)).loadExistById(any());
		then(disconnectOauthPort).should(times(1)).disconnect(any());
		then(disablePlanPort).should(times(1)).disable(any());
		then(userRepository).should(times(1)).save(any());
		assertThat(user.isDeleted()).isTrue();
	}
}
