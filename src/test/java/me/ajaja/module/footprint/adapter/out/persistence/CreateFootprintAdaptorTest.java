package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.dto.FootprintRequest;
import me.ajaja.module.footprint.mapper.FootprintMapperImpl;

@ContextConfiguration(classes = {CreateFootprintAdaptor.class, FootprintMapperImpl.class, FootprintFactory.class})
class CreateFootprintAdaptorTest extends JpaTestSupport {
	@Autowired
	private CreateFootprintAdaptor createFootprintAdaptor;

	@Autowired
	private FootprintFactory footprintFactory;

	@Test
	@DisplayName("자유 형식 발자취 생성 매핑 기능 구현 테스트")
	void create_FreeFootprint_Success() {
		// given
		Long userId = 1L;

		FootprintRequest.Create param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("title", "title")
			.set("type", Footprint.Type.FREE)
			.set("content", "content")
			.sample();

		Footprint freeFootprint = footprintFactory.create(userId, param);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(freeFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}
}
