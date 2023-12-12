package com.newbarams.ajaja.module.user.application.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

class ChangeReceiveTypeServiceTest extends MockTestSupport {
	@InjectMocks
	private ChangeReceiveTypeService changeReceiveTypeService;

	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private UserRepository userRepository;

	@ParameterizedTest
	@EnumSource(User.ReceiveType.class)
	@DisplayName("리마인드를 지원하는 종류로 변경할 수 있어야한다.")
	void change_Success(User.ReceiveType type) {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("email", new Email("Ajaja@me.com"))
			.sample();

		given(retrieveUserService.loadExistById(anyLong())).willReturn(user);
		given(userRepository.save(any())).willReturn(user);

		// when
		changeReceiveTypeService.change(user.getId(), type);

		// then
		then(retrieveUserService).should(times(1)).loadExistById(anyLong());
		then(userRepository).should(times(1)).save(any());
	}
}
