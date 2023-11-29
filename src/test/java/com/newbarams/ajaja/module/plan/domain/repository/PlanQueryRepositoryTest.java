package com.newbarams.ajaja.module.plan.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.module.plan.domain.Content;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@SpringBootTest
@Transactional
class PlanQueryRepositoryTest extends MonkeySupport {
	private final FixtureMonkey monkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.register(Plan.class, fixture -> fixture.giveMeBuilder(Plan.class)
			.set("id", Arbitraries.longs().greaterOrEqual(0))
			.set("messages", List.of(new Message("test")))
			.set("ajajas", Collections.EMPTY_LIST)
		)
		.register(Content.class, fixture -> fixture.giveMeBuilder(Content.class)
			.set("description", Arbitraries.strings().ofMaxLength(255)))
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.useExpressionStrictMode()
		.build();

	@Autowired
	private PlanQueryRepository planQueryRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private UserRepository userRepository;

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

	@Test
	@DisplayName("해마다 작성된 계획들을 모두 가져온다.")
	void findAllPlanInfoByUserId_Success_WithNoException() {
		// given
		Email email = new Email("yamsang2002@naver.com");
		User user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", email)
			.set("isDeleted", false)
			.sample());

		Plan plan1 = monkey.giveMeBuilder(Plan.class)
			.set("userId", user.getId())
			.set("status", new PlanStatus(true))
			.set("createdAt", Instant.now())
			.sample();

		Plan plan2 = monkey.giveMeBuilder(Plan.class)
			.set("userId", user.getId())
			.set("status", new PlanStatus(true))
			.set("createdAt", Instant.now().minus(365, ChronoUnit.DAYS))
			.sample();

		planRepository.saveAll(List.of(plan1, plan2));

		// when
		List<PlanInfoResponse.GetPlan> plans = planQueryRepository.findAllPlanByUserId(1L);

		// then
		assertThat(plans.size()).isEqualTo(2);
	}
}
