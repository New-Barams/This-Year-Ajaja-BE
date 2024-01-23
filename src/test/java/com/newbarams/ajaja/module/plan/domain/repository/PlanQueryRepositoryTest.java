package com.newbarams.ajaja.module.plan.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.domain.PlanStatus;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapper;

@SpringBootTest
@Transactional
class PlanQueryRepositoryTest extends MonkeySupport {
	@Autowired
	private PlanQueryRepository planQueryRepository;
	@Autowired
	private PlanRepository planRepository;
	@Autowired
	private UserJpaRepository userRepository;
	@Autowired
	private UserMapper userMapper;

	private User user;
	private Plan plan;

	@BeforeEach
	void deleteAll() {
		UserEntity entity = userMapper.toEntity(User.init(1L, "+82 1012345678", "ajaja@me.com"));
		user = userMapper.toDomain(userRepository.save(entity));

		plan = sut.giveMeBuilder(Plan.class)
			.set("userId", this.user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sample();
	}

	@Nested
	@DisplayName("findPlanDetailByIdAndOptionalUser 테스트")
	class FindPlanDetailByIdAndOptionalUserTest {
		@Test
		@DisplayName("사용자 ID를 입력하지 않고 상세 조회하면 작성자 정보에서 작성자 여부와 좋아요 여부는 false가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithUserIdNotEntered() { // no ajaja pressed query
			// given
			Plan save = planRepository.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				planQueryRepository.findPlanDetailByIdAndOptionalUser(null, save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().getNickname()).isEqualTo(user.getNickname().getNickname());
			assertThat(detail.getWriter().isOwner()).isFalse();
			assertThat(detail.getWriter().isAjajaPressed()).isFalse();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}

		@Test
		@DisplayName("작성자의 ID로 상세 조회하면 작성자 정보에서 작성자 여부는 true가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithWriterUserId() {
			// given
			Plan save = planRepository.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				planQueryRepository.findPlanDetailByIdAndOptionalUser(user.getId(), save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().getNickname()).isEqualTo(user.getNickname().getNickname());
			assertThat(detail.getWriter().isOwner()).isTrue();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}

		@Test
		@DisplayName("다른 사람의 id로 상세 조회하면 작성자 정보에서 작성자 여부와 좋아요 여부는 false가 되어야 한다.")
		void findPlanDetailByIdAndOptionalUser_Success_WithStrangerId() {
			// given
			Long strangerId = user.getId() + 1;
			Plan save = planRepository.save(plan);

			// when
			Optional<PlanResponse.Detail> result =
				planQueryRepository.findPlanDetailByIdAndOptionalUser(strangerId, save.getId());

			// then
			assertThat(result).isNotEmpty();

			PlanResponse.Detail detail = result.get();
			assertThat(detail.getWriter().isOwner()).isFalse();
			assertThat(detail.getWriter().isAjajaPressed()).isFalse();
			assertThat(detail.getId()).isEqualTo(save.getId());
			assertThat(detail.isPublic()).isEqualTo(save.getStatus().isPublic());
		}
	}

	@Test
	@DisplayName("올해의 계획들만 사용자의 아이디로 가져올 수 있어야 한다.")
	void findAllCurrentPlansByUserId_Success() {
		// given
		int expectedSize = 4;
		Long userId = sut.giveMeOne(Long.class);

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", userId)
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sampleList(expectedSize);

		planRepository.saveAll(plans);

		// when
		List<Plan> currentPlans = planQueryRepository.findAllCurrentPlansByUserId(userId);

		// then
		assertThat(currentPlans).isNotEmpty().hasSize(expectedSize);
	}

	@Test
	@DisplayName("계획의 ID를 이용해 하나의 계획을 가져올 수 있다.")
	void findByUserIdAndPlanId_Success_WithNoException() {
		Plan savedPlan = planRepository.save(plan);

		Plan plan = planQueryRepository.findByUserIdAndPlanId(savedPlan.getUserId(), savedPlan.getId());
		assertThat(plan).isNotNull();
	}

	@Test
	@DisplayName("계획의 ID를 이용해 하나의 계획을 가져올 수 있다.")
	void findByUserIdAndPlanId_Fail_ByNotMatchPlanIdAndUserId() {
		Plan savedPlan = planRepository.save(plan);
		Long userId = savedPlan.getUserId() + 1L;

		assertThatException().isThrownBy(
			() -> planQueryRepository.findByUserIdAndPlanId(userId, savedPlan.getId())
		);
	}

	@Test
	@DisplayName("최신순 or 인기순 정렬 시 지정된 개수만큼의 계획을 받는다.")
	void findAllByCursorAndSorting_Default_Success() {
		int pageSize = 3;

		List<Plan> plans = sut.giveMeBuilder(Plan.class)
			.set("userId", user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
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
			.set("userId", user.getId())
			.set("status", new PlanStatus(true, true))
			.set("ajajas", Collections.EMPTY_LIST)
			.sampleList(pageSize);

		planRepository.saveAll(plans);

		PlanRequest.GetAll latestReq = new PlanRequest.GetAll("latest", true, null, null);

		List<PlanResponse.GetAll> latestRes = planQueryRepository.findAllByCursorAndSorting(latestReq);

		for (int i = 0; i < pageSize - 1; i++) {
			assertThat(latestRes.get(i).getId()).isGreaterThan(latestRes.get(i + 1).getId());
		}
	}

	@Test
	@DisplayName("해당 시간에 리마인드 가능한 계획들을 조회한다.")
	void findAllRemindablePlan_Success_WithNoException() {
		// when,then
		Assertions.assertThatNoException().isThrownBy(
			() -> planQueryRepository.findAllRemindablePlan("MORNING", TimeValue.now())
		);
	}
}
