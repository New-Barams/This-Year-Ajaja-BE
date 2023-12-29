package com.newbarams.ajaja.module.user.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.application.port.out.ChangeReceiveTypePort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

class ChangeReceiveTypeServiceTest extends MockTestSupport {
	@InjectMocks
	private ChangeReceiveTypeService changeReceiveTypeService;

	@Mock
	private ChangeReceiveTypePort changeReceiveTypePort;

	@ParameterizedTest
	@EnumSource(User.ReceiveType.class)
	@DisplayName("리마인드를 지원하는 종류로 변경할 수 있어야한다.")
	void change_Success(User.ReceiveType type) {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("email", new Email("Ajaja@me.com"))
			.sample();

		willDoNothing().given(changeReceiveTypePort).change(anyLong(), any());

		// when
		changeReceiveTypeService.change(user.getId(), type);

		// then
		then(changeReceiveTypePort).should(times(1)).change(anyLong(), any());
	}
}
