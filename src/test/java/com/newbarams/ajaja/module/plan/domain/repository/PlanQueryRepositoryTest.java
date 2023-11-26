package com.newbarams.ajaja.module.plan.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@SpringBootTest
@Transactional
class PlanQueryRepositoryTest {
	private final FixtureMonkey monkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.register(Plan.class, fixture -> fixture.giveMeBuilder(Plan.class)
			.set("id", Arbitraries.longs().greaterOrEqual(0))
			.set("messages", List.of(new Message("test")))
			.set("ajajas", Collections.EMPTY_LIST)
		)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.useExpressionStrictMode()
		.build();

	@Autowired
	private PlanQueryRepository planQueryRepository;

	@Autowired
	private PlanRepository planRepository;

	@Test
	@DisplayName("올해의 계획들만 사용자의 아이디로 가져올 수 있어야 한다.")
	void findAllCurrentPlansByUserId_Success() {
		// given
		int expectedSize = 4;
		Long userId = monkey.giveMeOne(Long.class);

		List<Plan> plans = monkey.giveMeBuilder(Plan.class)
			.set("userId", userId)
			.set("status", new PlanStatus(true))
			.sampleList(expectedSize);

		planRepository.saveAll(plans);

		// when
		List<Plan> currentPlans = planQueryRepository.findAllCurrentPlansByUserId(userId);

		// then
		assertThat(currentPlans).isNotEmpty().hasSize(expectedSize);
	}
}
