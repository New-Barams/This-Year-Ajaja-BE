package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.KptFootprint;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.footprint.mapper.FreeFootprintMapperImpl;
import me.ajaja.module.footprint.mapper.KptFootprintMapperImpl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ContextConfiguration(classes = {
	CreateFootprintAdaptor.class,
	FreeFootprintMapperImpl.class,
	KptFootprintMapperImpl.class
})
class CreateFootprintAdaptorTest extends JpaTestSupport {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();

	@Autowired
	private CreateFootprintAdaptor createFootprintAdaptor;

	@Test
	void create_FreeFootprint_Success() {
		// given
		FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
		String content = "content";
		FreeFootprint freeFootprint = FootprintFactory.createFreeFootprint(param, content);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(freeFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}

	@Test
	void create_KptFootprint_Success() {
		// given
		FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
		String keepContent = "keepContent";
		String problemContent = "problemContent";
		String tryContent = "tryContent";

		KptFootprint kptFootprint = FootprintFactory.createKptFootprint(param, keepContent, problemContent, tryContent);

		// when
		Long footprintEntityId = createFootprintAdaptor.create(kptFootprint);

		// then
		assertThat(footprintEntityId).isNotNull();
	}
}