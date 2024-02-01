package me.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.user.application.port.out.ApplyChangePort;
import me.ajaja.module.user.domain.Email;
import me.ajaja.module.user.domain.PhoneNumber;
import me.ajaja.module.user.domain.User;

class ChangeRemindTypeServiceTest extends MockTestSupport {
	@InjectMocks
	private ChangeRemindTypeService changeReceiveTypeService;

	@Mock
	private RetrieveUserService retrieveUserService;
	@Mock
	private ApplyChangePort applyChangePort;

	@ParameterizedTest
	@EnumSource(User.RemindType.class)
	@DisplayName("리마인드를 지원하는 종류로 변경할 수 있어야한다.")
	void change_Success(User.RemindType type) {
		// given
		User user = sut.giveMeBuilder(User.class)
			.set("phoneNumber", new PhoneNumber("01012345678"))
			.set("email", Email.init("ajaja@me.com"))
			.sample();

		given(retrieveUserService.loadExistById(anyLong())).willReturn(user);
		willDoNothing().given(applyChangePort).apply(any());

		// when
		changeReceiveTypeService.change(user.getId(), type);

		// then
		assertThat(user.getRemindType()).isEqualTo(type);
		then(retrieveUserService).should(times(1)).loadExistById(anyLong());
		then(applyChangePort).should(times(1)).apply(any());
	}
}
