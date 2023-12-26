package com.newbarams.ajaja.module.plan.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@SpringBootTest
@Transactional
class PlanQueryRepositoryTest {
	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(new FailoverIntrospector(
			List.of(
				FieldReflectionArbitraryIntrospector.INSTANCE,
				ConstructorPropertiesArbitraryIntrospector.INSTANCE
			)
		))
		.register(Plan.class, fixture -> fixture.giveMeBuilder(Plan.class)
			.set("id", Arbitraries.longs().greaterOrEqual(0))
			.set("messages", List.of(new Message("test", 3, 15)))
			.set("ajajas", Collections.emptyList())
		)
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

	private User saved;
	private Plan plan;

	@BeforeEach
	void deleteAll() {
		User user = User.init("email@naver.com", 1L);

		saved = userRepository.save(user);

		plan = sut.giveMeBuilder(Plan.class)
			.set("userId", saved.getId())
			.set("status", new PlanStatus(true))
			.sample();
	}

	@Test
	@DisplayName("올해의 계획들만 사용자의 아이디로 가져올 수 있어야 한다.")
	void findAllCurrentPlansByUserId_Success() {
		// given
		int expectedSize = 4;
		Long userId = sut.giveMeOne(Long.class);

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
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
	@DisplayName("계획의 ID를 이용해 하나의 계획을 가져올 수 있다.")
	void findById_Success() {
		Plan savedPlan = planRepository.save(plan);

		Optional<PlanResponse.GetOne> response = planQueryRepository.findById(savedPlan.getId(), saved.getId());
		assertThat(response).isNotEmpty();
	}

	@Test
	@DisplayName("계획 id가 존재하지 않으면 가져올 수 없다.")
	void findById_Fail_By_Not_Exist_PlanId() {
		planRepository.save(plan);

		Optional<PlanResponse.GetOne> response = planQueryRepository.findById(-1L, saved.getId());
		assertThat(response).isEmpty();
	}

	@Test
	@DisplayName("최신순 or 인기순 정렬 시 지정된 개수만큼의 계획을 받는다.")
	void findAllByCursorAndSorting_Default_Success() {
		int pageSize = 3;

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", saved.getId())
			.set("status", new PlanStatus(true))
			.sampleList(10);

		planRepository.saveAll(plans);

		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);
		PlanRequest.GetAll ajajaReq = new PlanRequest.GetAll("ajaja", true, null, null);

		List<PlanResponse.GetAll> latestRes = planQueryRepository.findAllByCursorAndSorting(latestReq);
		List<PlanResponse.GetAll> ajajaRes = planQueryRepository.findAllByCursorAndSorting(ajajaReq);

		assertThat(latestRes).hasSize(pageSize);
		assertThat(ajajaRes).hasSize(pageSize);
	}

	@Test
	@DisplayName("최신순 정렬 시 아이디 순으로 정렬된 계획들을 반환한다.")
	void findAllByCursorAndSorting_With_Latest_Condition_Success() {
		int pageSize = 3;

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", saved.getId())
			.set("status", new PlanStatus(true))
			.sampleList(pageSize);

		planRepository.saveAll(plans);

		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);

		List<PlanResponse.GetAll> latestRes = planQueryRepository.findAllByCursorAndSorting(latestReq);

		for (int i = 0; i < pageSize - 1; i++) {
			assertThat(latestRes.get(i).id()).isGreaterThan(latestRes.get(i + 1).id());
		}
	}

	@Test
	@DisplayName("해당 시간에 리마인드 가능한 계획들을 조회한다.")
	void findAllRemindablePlan_Success_WithNoException() {
		// when,then
		Assertions.assertThatNoException().isThrownBy(
			() -> planQueryRepository.findAllRemindablePlan("MORNING", new TimeValue())
		);
	}
}
