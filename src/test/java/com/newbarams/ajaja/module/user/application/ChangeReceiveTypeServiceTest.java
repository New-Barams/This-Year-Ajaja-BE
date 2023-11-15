package com.newbarams.ajaja.module.user.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;

class ChangeReceiveTypeServiceTest extends MockTestSupport {
	@InjectMocks
	private ChangeReceiveTypeService changeReceiveTypeService;

	@Mock
	private RetrieveUserService retrieveUserService;

	@ParameterizedTest
	@ValueSource(strings = {"kakao", "email", "both"})
	@DisplayName("유효한 리마인드 타입이 들어오면 예외가 발생하지 않고 수정되어야 한다.")
	void change_Success_WithoutException(String receiveType) {
		// given
		User user = monkey.giveMeBuilder(User.class)
			.set("email", new Email("gmlwh124@Naver.com"))
			.sample();

		given(retrieveUserService.loadExistUserById(anyLong())).willReturn(user);

		// when
		changeReceiveTypeService.change(user.getId(), receiveType);

		// then
		then(retrieveUserService).should(times(1)).loadExistUserById(anyLong());
	}
}
