package me.ajaja.module.footprint.application.port;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import lombok.RequiredArgsConstructor;
import me.ajaja.common.support.MockTestSupport;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Writer;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.user.application.port.out.RetrieveUserPort;
import me.ajaja.module.user.domain.Email;
import me.ajaja.module.user.domain.PhoneNumber;
import me.ajaja.module.user.domain.User;

@RequiredArgsConstructor
class CreateFootprintServiceTest extends MockTestSupport {
	private static final String PHONE_NUMBER = "01012345678";
	private static final String DEFAULT_EMAIL = "Ajaja@me.com";

	@InjectMocks
	private CreateFootprintService createFootprintService;

	@Mock
	private FootprintFactory footprintFactory;

	@Mock
	private CreateFootprintPort createFootprintPort;

	@Mock
	private RetrieveUserPort retrieveUserPort;

	@Mock
	private FindPlanPort findPlanPort;

	private User user;

	private Plan plan;

	private Target target;

	private FootprintParam.Create param;

	private Footprint footprint;

	@BeforeEach
	void setUpFixture() {
		user = sut.giveMeBuilder(User.class)
			.set("phoneNumber", new PhoneNumber(PHONE_NUMBER))
			.set("email", Email.init(DEFAULT_EMAIL))
			.sample();

		plan = sut.giveMeOne(Plan.class);

		target = new Target(plan.getId(), plan.getPlanTitle());

		param = sut.giveMeOne(FootprintParam.Create.class);

		footprint = sut.giveMeBuilder(Footprint.class)
			.set("type", Footprint.Type.FREE)
			.set("content", "contents")
			.sample();
	}

	@Test
	@DisplayName("발자취 생성에 성공 한다.")
	void createFootPrint_Success_With_NoExceptions() {
		// given
		long expectedFootprintId = 1l;

		when(retrieveUserPort.loadById(user.getId())).thenReturn(Optional.of(user));
		when(findPlanPort.findById(plan.getId())).thenReturn(Optional.of(plan));
		when(footprintFactory.create(any(Target.class), any(Writer.class), eq(param))).thenReturn(footprint);
		when(createFootprintPort.create(footprint)).thenReturn(expectedFootprintId);

		// when
		long actualId = createFootprintService.create(user.getId(), target.getId(), param);

		// then
		assertThat(actualId).isEqualTo(expectedFootprintId);
		verify(retrieveUserPort, times(1)).loadById(user.getId());
		verify(findPlanPort, times(1)).findById(plan.getId());
		verify(footprintFactory, times(1)).create(any(Target.class), any(Writer.class), eq(param));
		verify(createFootprintPort, times(1)).create(footprint);
	}

	@Test
	@DisplayName("발자취 대상 계획이 존재하지 않으면 발자취 생성에 실패한다.")
	void createFootprint_Fail_By_NotFountTarget() {

		when(retrieveUserPort.loadById(user.getId())).thenReturn(Optional.of(user));
		when(findPlanPort.findById(plan.getId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> createFootprintService.create(user.getId(), target.getId(), param))
			.isInstanceOf(AjajaException.class)
			.hasMessage((NOT_FOUND_PLAN.getMessage()));
	}
}
