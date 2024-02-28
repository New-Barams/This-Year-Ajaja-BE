package me.ajaja.module.footprint.application.port;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.dto.FootprintRequest;

class CreateFootprintServiceTest extends MockTestSupport {
	private static final String PHONE_NUMBER = "01012345678";
	private static final String DEFAULT_EMAIL = "Ajaja@me.com";

	@InjectMocks
	private CreateFootprintService createFootprintService;

	@Mock
	private FootprintFactory footprintFactory;

	@Mock
	private CreateFootprintPort createFootprintPort;

	private FootprintRequest.Create param;
	private Footprint footprint;

	@BeforeEach
	void setUpFixture() {
		param = sut.giveMeOne(FootprintRequest.Create.class);

		footprint = sut.giveMeBuilder(Footprint.class)
			.set("type", Footprint.Type.FREE)
			.set("content", "contents")
			.sample();
	}

	@Test
	@DisplayName("발자취 생성에 성공 한다.")
	void createFootPrint_Success_With_NoExceptions() {
		// given
		Long userId = 1L;
		Long expectedFootprintId = 1L;

		when(footprintFactory.init(anyLong(), eq(param))).thenReturn(footprint);
		when(createFootprintPort.create(footprint)).thenReturn(expectedFootprintId);

		// when
		createFootprintService.create(userId, param);

		// then
		assertAll("verify stubbing method",
			() -> verify(footprintFactory, times(1)).init(anyLong(), eq(param)),
			() -> verify(createFootprintPort, times(1)).create(footprint)
		);
	}
}
