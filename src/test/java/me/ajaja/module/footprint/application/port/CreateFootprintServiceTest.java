package me.ajaja.module.footprint.application.port;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.footprint.application.CreateFootprintService;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.application.port.out.CreateTagPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.FootprintRequest;

class CreateFootprintServiceTest extends MockTestSupport {
	@InjectMocks
	private CreateFootprintService createFootprintService;

	@Mock
	private CreateFootprintPort createFootprintPort;

	@Mock
	private CreateTagPort createTagPort;

	private FootprintRequest.Create param;

	@BeforeEach
	void setUpFixture() {
		param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("title", "title")
			.set("type", Footprint.Type.FREE)
			.set("content", "contents")
			.set("tags", List.of("tag1", "tag2"))
			.sample();
	}

	@Test
	@DisplayName("발자취 생성에 성공 한다.")
	void createFootPrint_Success_With_NoExceptions() {
		// given
		param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("title", "title")
			.set("type", Footprint.Type.FREE)
			.set("content", "contents")
			.set("tags", List.of("tag1", "tag2"))
			.sample();

		Long userId = 1L;
		Long expectedFootprintId = 1L;

		when(createFootprintPort.create(any())).thenReturn(expectedFootprintId);
		doNothing().when(createTagPort).create(anyLong(), anyList());

		// when
		createFootprintService.create(userId, param);

		// then
		assertAll(
			() -> verify(createFootprintPort, times(1)).create(any()),
			() -> verify(createTagPort, times(1)).create(anyLong(), anyList())
		);
	}
}
