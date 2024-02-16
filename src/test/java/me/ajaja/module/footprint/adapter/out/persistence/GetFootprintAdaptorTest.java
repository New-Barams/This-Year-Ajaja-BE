package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.footprint.mapper.FootprintMapperImpl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ContextConfiguration(classes = {
	CreateFootprintAdaptor.class,
	GetFootprintAdaptor.class,
	FootprintMapperImpl.class
})
class GetFootprintAdaptorTest extends JpaTestSupport {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();

	@Autowired
	private CreateFootprintAdaptor createFootprintAdaptor;

	@Autowired
	private GetFootprintAdaptor getFootprintAdaptor;

	@Test
	@DisplayName("자유 형식 발자취 조회 매핑 기능 구현 테스트")
	void get_FreeFootprint_Success() {
		// given
		FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
		String content = "content";
		FreeFootprint freeFootprint = FootprintFactory.createFreeFootprint(param, content);

		Long createdId = createFootprintAdaptor.create(freeFootprint);

		// when
		Footprint footprint = getFootprintAdaptor.getFootprint(createdId);

		// then
		assertThat(footprint.getId()).isEqualTo(createdId);
	}

	@Test
	@DisplayName("Kpt 형식 발자취 조회 매핑 기능 구현 테스트")
	void get_KptFootprint_Success() {
		// given
		FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
		String keepContent = "keepContent";
		String problemContent = "problemContent";
		String tryContent = "tryContent";
		KptFootprint kptFootprint = FootprintFactory.createKptFootprint(param, keepContent, problemContent, tryContent);

		Long createdId = createFootprintAdaptor.create(kptFootprint);

		// when
		Footprint footprint = getFootprintAdaptor.getFootprint(createdId);

		// then
		assertThat(footprint.getId()).isEqualTo(createdId);
	}
}
