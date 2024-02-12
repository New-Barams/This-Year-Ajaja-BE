package me.ajaja.module.footprint.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.sql.Blob;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import me.ajaja.common.support.MonkeySupport;
import me.ajaja.module.footprint.dto.FootprintParam;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class FootprintTest extends MonkeySupport {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();

	@Nested
	@DisplayName("유형별 발자취 도메인 생성을 성공한다")
	class CreateFootprintTest {
		@Test
		@DisplayName("자유 형식 발자취 도메인 생성을 성공한다.")
		public void createFreeFootprint_Success_WithNoException() {
			assertThatNoException().isThrownBy(
				() -> {
					FootprintParam.Create createParam = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
					Blob content = fixtureMonkey.giveMeOne(Blob.class);

					FreeFootprint freeFootprint = new FreeFootprint(createParam, content);
				}
			);
		}

		@Test
		@DisplayName("KPT 형식 발자취 도메인 생성을 성공한다.")
		public void createKptFootprint_Success_WithNoException() {
			assertThatNoException().isThrownBy(
				() -> {
					FootprintParam.Create createParam = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
					Blob keepContent = fixtureMonkey.giveMeOne(Blob.class);
					Blob problemContent = fixtureMonkey.giveMeOne(Blob.class);
					Blob tryContent = fixtureMonkey.giveMeOne(Blob.class);

					KptFootprint kptFootprint = new KptFootprint(createParam, keepContent, problemContent, tryContent);
				}
			);
		}
	}
}
