package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.application.port.out.DisablePlanPort;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

class WithdrawServiceTest extends MockTestSupport {
	@InjectMocks
	private WithdrawService withdrawService;

	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private DisconnectOauthPort disconnectOauthPort;
	@Mock
	private DisablePlanPort disablePlanPort;
	@Mock
	private ApplyChangePort applyChangePort;

	@Test
	void withdraw_Success() {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("email", new Email("Ajaja@me.com"))
			.set("deleted", false)
			.sample();

		given(retrieveUserService.loadExistById(anyLong())).willReturn(user);
		willDoNothing().given(disconnectOauthPort).disconnect(any());
		willDoNothing().given(disablePlanPort).disable(any());
		willDoNothing().given(applyChangePort).apply(any());

		// when
		withdrawService.withdraw(user.getId());

		// then
		assertThat(user.isDeleted()).isTrue();
		then(retrieveUserService).should(times(1)).loadExistById(anyLong());
		then(disconnectOauthPort).should(times(1)).disconnect(any());
		then(disablePlanPort).should(times(1)).disable(any());
		then(applyChangePort).should(times(1)).apply(any());
	}
}
