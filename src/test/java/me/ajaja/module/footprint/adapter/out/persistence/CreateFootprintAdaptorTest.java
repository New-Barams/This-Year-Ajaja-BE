package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Writer;
import me.ajaja.module.footprint.dto.FootprintParam;
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
		Target target = new Target(1L, "Example Plan");
		Writer writer = new Writer(1L, "Example Nickname");
		FootprintParam.Create param = sut.giveMeBuilder(FootprintParam.Create.class)
			.set("type", Footprint.Type.FREE)
			.set("content", "content")
			.sample();

		Footprint freeFootprint = footprintFactory.create(target, writer, param);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(freeFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}
}
