package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.footprint.mapper.FootprintMapperImpl;

@ContextConfiguration(classes = {
	CreateFootprintAdaptor.class,
	FootprintMapperImpl.class
})
class CreateFootprintAdaptorTest extends JpaTestSupport {
	@Autowired
	private CreateFootprintAdaptor createFootprintAdaptor;

	@Test
	@DisplayName("자유 형식 발자취 생성 매핑 기능 구현 테스트")
	void create_FreeFootprint_Success() {
		// given
		FootprintParam.Create param = sut.giveMeOne(FootprintParam.Create.class);
		String content = "content";
		FreeFootprint freeFootprint = FootprintFactory.freeTemplate(param, content);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(freeFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}

	@Test
	@DisplayName("자유 형식 발자취 생성 매핑 기능 구현 테스트")
	void create_KptFootprint_Success() {
		// given
		FootprintParam.Create param = sut.giveMeOne(FootprintParam.Create.class);
		String keepContent = "keepContent";
		String problemContent = "problemContent";
		String tryContent = "tryContent";

		KptFootprint kptFootprint = FootprintFactory.kptTemplate(param, keepContent, problemContent, tryContent);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(kptFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}
}
