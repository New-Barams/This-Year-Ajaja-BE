package me.ajaja.module.footprint.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;
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
	@DisplayName("조건에 맞는 입력 값에 따라 유형별 발자취 도메인 생성에 성공 한다.")
	class CreateFootprintTest {
		@Test
		@DisplayName("자유 형식 발자취 도메인 생성에 성공 한다.")
		void create_FreeFootprint_Success_WithNoException() {
			FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
			String content = "content";

			assertThatNoException().isThrownBy(() -> {
				FootprintFactory.freeTemplate(param, content);
			});
		}

		@Test
		@DisplayName("KPT 형식 발자취 도메인 생성에 성공 한다.")
		void create_KptFootprint_Success_WithNoException() {
			FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
			String keepContent = "keepContent";
			String problemContent = "problemContent";
			String tryContent = "tryContent";

			assertThatNoException().isThrownBy(() -> {
				FootprintFactory.kptTemplate(param, keepContent, problemContent, tryContent);
			});
		}
	}

	@Test
	@DisplayName("발자취 항목에 대한 값이 null 일 때 예외가 발생 한다.")
	void create_Fail_ByNoneContnets() {
		FootprintParam.Create param = fixtureMonkey.giveMeOne(FootprintParam.Create.class);
		String content = null;

		assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
			() -> FootprintFactory.freeTemplate(param, content)
		);
	}
}
